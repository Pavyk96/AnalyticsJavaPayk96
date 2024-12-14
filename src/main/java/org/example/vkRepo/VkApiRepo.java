package org.example.vkRepo;

import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.base.City;
import com.vk.api.sdk.objects.users.Fields;
import com.vk.api.sdk.objects.users.SubscriptionsItem;
import com.vk.api.sdk.objects.users.UserFull;
import org.example.model.Student;
import org.example.modelRepo.StudentRepo;
import org.example.parser.ModelParser;
import org.example.util.DatabaseUtil;
import org.example.util.HibernateUtil;
import org.hibernate.Session;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class VkApiRepo {
    private final Long APP_ID;
    private final String CODE;
    private final VkApiClient vkApiClient;
    private final UserActor userActor;

    public VkApiRepo() throws IOException {
        Properties properties = new Properties();
        InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties");
        properties.load(input);

        this.APP_ID = Long.parseLong(properties.getProperty("app.id"));
        this.CODE = properties.getProperty("code");

        TransportClient transportClient = new HttpTransportClient();
        this.vkApiClient = new VkApiClient(transportClient);
        this.userActor = new UserActor(APP_ID, CODE);
    }

    public City findUserCityByFullName(String fullName) throws ApiException, ClientException {
        List<UserFull> city = vkApiClient.users()
                .search(userActor)
                .q(fullName)
                .count(1)
                .fields(Fields.CITY)
                .execute()
                .getItems();

        return city.isEmpty() ? null : city.get(0).getCity();
    }

    public Long findStudentId (String fullName) throws ClientException, ApiException {
        List<UserFull> list = vkApiClient.users()
                .search(userActor)
                .q(fullName)
                .count(1)
                .execute().getItems();

        return list.isEmpty() ? null : list.get(0).getId();
    }

    public int findStudentItGroups(Long id) throws ClientException, ApiException {
        try {
            List<SubscriptionsItem> subscriptionsItemList =
                    vkApiClient.users()
                            .getSubscriptionsExtended(userActor)
                            .userId(id)
                            .execute()
                            .getItems();

            if (subscriptionsItemList.isEmpty())
                return 0;

            return countGroup(subscriptionsItemList);
        }catch (Exception e) {
            System.out.println("Id студента не найдено");
        }

        return 0;
    }

        public int countGroup(List<SubscriptionsItem> subscriptionsItemList) {
            int count = 0;
            for (SubscriptionsItem item : subscriptionsItemList) {
                String name = item.getUsersSubscriptionsItemAsGroupFull().getName();
                if (isNameContainsIt(name)) {
                    count++;
                }
            }

            return count;
        }

    public boolean isNameContainsIt(String name) {
        if (name == null || name.isEmpty()) {
            return false;
        }

        List<String> keywords = Arrays.asList(
                "IT", "информационные технологии", "технологии", "разработка", "software", "hardware",
                "тестирование", "QA", "сети", "базы данных", "цифровизация",

                "программирование", "кодинг", "coding", "код", "программисты", "разработчики",
                "разработка приложений", "software development", "backend", "frontend",
                "devops", "fullstack", "проектирование систем",

                "Java", "Python", "C++", "C#", "JavaScript", "TypeScript", "PHP", "Ruby",
                "Swift", "Kotlin", "Go", "Rust", "SQL", "R", "Dart", "MATLAB",

                "spring", "django", "flask", "angular", "react", "vue", "node", "express",
                "bootstrap", "tensorflow", "pytorch", "keras", "kubernetes", "docker",

                "искусственный интеллект", "машинное обучение", "data science", "big data",
                "AI", "ML", "нейросети", "нейронные сети", "data", "анализ данных",

                "web", "веб", "веб-разработка", "сайты", "сайт", "хостинг", "html", "css", "ux", "ui",
                "дизайн интерфейсов", "разработка интерфейсов",

                "кибербезопасность", "информационная безопасность", "cybersecurity",
                "антивирусы", "вирусы", "шифрование", "защита данных",

                "облако", "облачные технологии", "cloud", "aws", "azure", "gcp", "google cloud",
                "микросервисы",

                "робототехника", "IoT", "интернет вещей", "embedded", "3D", "виртуальная реальность",
                "VR", "AR", "метавселенная", "блокчейн", "криптовалюты"
        );

        String lowerCaseName = name.toLowerCase();

        for (String keyword : keywords) {
            if (lowerCaseName.contains(keyword.toLowerCase())) {
                return true;
            }
        }

        return false;
    }


    public static void UppdateStudentInf() throws IOException, ClientException, ApiException, InterruptedException {
        VkApiRepo vkApiRepo = new VkApiRepo();

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            for (Student student : ModelParser.studentList) {
                student.setCity(vkApiRepo.findUserCityByFullName(student.getFullName()));
                Long id = vkApiRepo.findStudentId(student.getFullName());
                student.setCountOfItGroups(vkApiRepo.findStudentItGroups(id));

                String city = student.getCity() != null ? student.getCity().getTitle() : "Неизвестный город";

                StudentRepo group = new StudentRepo(
                        student.getId(),
                        student.getScore(),
                        student.getCountOfItGroups(),
                        city
                );

                // Сохранение студента в базе через отдельный метод
                DatabaseUtil.saveStudent(group);

                Thread.sleep(700); // Пауза между запросами
            }
        } finally {
            HibernateUtil.shutdown();
        }
    }


}
