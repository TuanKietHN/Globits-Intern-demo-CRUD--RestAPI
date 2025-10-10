package vn.globits.demo.init;

import com.github.javafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import vn.globits.demo.domain.Person;
import vn.globits.demo.domain.User;
import vn.globits.demo.repository.PersonRepository;
import vn.globits.demo.repository.UserRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    private final PersonRepository personRepository;
    private final UserRepository userRepository;
    private final Faker faker = new Faker();

    public DataInitializer(PersonRepository personRepository, UserRepository userRepository) {
        this.personRepository = personRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        System.out.println("🚀 Bắt đầu tạo dữ liệu mẫu ngẫu nhiên...");

        for (int i = 0; i < 20; i++) { // tạo 20 user/person ngẫu nhiên
            Person person = new Person();
            person.setFullName(faker.name().fullName());
            person.setAddress(faker.address().fullAddress());
            person.setGender(faker.options().option("Male", "Female"));
            person.setPhoneNumber(faker.phoneNumber().phoneNumber());
            person.setBirthdate(faker.date().birthday().toInstant()
                    .atZone(java.time.ZoneId.systemDefault())
                    .toLocalDate());

            person = personRepository.save(person); // save person trước

            User user = new User();
            user.setEmail(faker.internet().emailAddress());
            user.setPassword("123456"); // hoặc random
            user.setActive(true);
            user.setPerson(person);

            userRepository.save(user);
        }

        System.out.println("✅ Dữ liệu ngẫu nhiên tạo xong!");
    }
}
