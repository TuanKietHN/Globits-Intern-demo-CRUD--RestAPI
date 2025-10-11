package vn.globits.demo.init;

import com.github.javafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import vn.globits.demo.domain.Person;
import vn.globits.demo.domain.User;
import vn.globits.demo.repository.PersonRepository;
import vn.globits.demo.repository.UserRepository;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Date;

@Component
@Transactional
public class DataInitializer implements CommandLineRunner {

    private final PersonRepository personRepository;
    private final UserRepository userRepository;

    public DataInitializer(PersonRepository personRepository, UserRepository userRepository) {
        this.personRepository = personRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        Faker faker = new Faker(new Locale("en"));
        Random rnd = new Random();

//        // --- SAFETY: chỉ tạo nếu DB trống (thay đổi nếu bạn muốn xóa rồi tạo lại) ---
//        if (personRepository.count() > 0 || userRepository.count() > 0) {
//            System.out.println("DB already has data -> skip seeding.");
//            return;
//        }

        // Nếu muốn mỗi lần khởi động reset DB, uncomment:
        userRepository.deleteAll();
        personRepository.deleteAll();

        List<Person> savedPersons = new ArrayList<>();

        // tạo 100 Person
        for (int i = 0; i < 100; i++) {
            Person p = new Person();
            p.setFullName(faker.name().fullName());
            p.setGender(rnd.nextBoolean() ? "Male" : "Female");

            // faker.date().birthday trả về java.util.Date -> convert sang LocalDate
            Date d = faker.date().birthday(18, 65);
            LocalDate ld = d.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            // trong file của bạn có 2 tên method (setBirthDate / setBirthdate).
            // Mình dùng setBirthDate; nếu compile lỗi, đổi thành setBirthdate
            p.setBirthDate(ld);

            p.setPhoneNumber(faker.phoneNumber().cellPhone());
            p.setAddress(faker.address().fullAddress());

            savedPersons.add(personRepository.save(p));
        }

        // tạo 100 User (kèm liên kết 1:1 tới Person tương ứng)
        for (int i = 0; i < 100; i++) {
            User u = new User();
            u.setEmail("user" + (i + 1) + "@example.com");
            u.setPassword("123456"); // nếu bạn dùng password encoder, thay bằng encoder.encode(...)
            u.setIsActive(true);
            // gán person tương ứng (không bắt buộc nếu person_id có thể NULL)
            u.setPerson(savedPersons.get(i));
            userRepository.save(u);
        }

        System.out.println("✅ Seeded 100 Person and 100 User records.");
    }
}
