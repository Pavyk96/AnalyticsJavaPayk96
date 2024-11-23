package org.example.vkRepo;

import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.base.City;
import com.vk.api.sdk.objects.users.Fields;
import com.vk.api.sdk.objects.users.UserFull;
import org.example.Model.Student;
import org.example.Parser.ModelParser;

import java.io.IOException;
import java.io.InputStream;
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

    private City findUserIdByFullName(String fullName) throws ApiException, ClientException {
        List<UserFull> city = vkApiClient.users()
                .search(userActor)
                .q(fullName)
                .count(1)
                .fields(Fields.CITY)
                .execute()
                .getItems();

        return city.isEmpty() ? null : city.get(0).getCity();
    }

    public static void UppdateStudentInf() throws IOException, ClientException, ApiException, InterruptedException {
        VkApiRepo vkApiRepo = new VkApiRepo();
        for(Student student : ModelParser.studentList) {
            student.setCity(vkApiRepo.findUserIdByFullName(student.getFullName()));
            System.out.println(student);
            Thread.sleep(500);
        }

    }

}
