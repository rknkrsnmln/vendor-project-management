package com.rkm.projectmanagement.system;

import com.rkm.projectmanagement.entities.Project;
import com.rkm.projectmanagement.entities.User;
import com.rkm.projectmanagement.entities.Vendor;
import com.rkm.projectmanagement.repository.ProjectRepository;
import com.rkm.projectmanagement.repository.UserRepository;
import com.rkm.projectmanagement.repository.VendorRepository;
import com.rkm.projectmanagement.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DBDataInitializer implements CommandLineRunner {


    private final ProjectRepository projectRepository;

    private final VendorRepository vendorRepository;

    private final UserService userService;


    public DBDataInitializer(ProjectRepository projectRepository, VendorRepository vendorRepository, UserService userService) {
        this.projectRepository = projectRepository;
        this.vendorRepository = vendorRepository;
        this.userService = userService;
    }

    @Override
    public void run(String... args) throws Exception {


        Project a1 = new Project();
        a1.setId("1250808601744904191");
        a1.setName("Deluminator");
        a1.setDescription("A Deluminator is a device invented by Albus Dumbledore that resembles a cigarette lighter. It is used to remove or absorb (as well as return) the light from any light source to provide cover to the user.");
        a1.setImageUrl("ImageUrl");

        Project a2 = new Project();
        a2.setId("1250808601744904192");
        a2.setName("Invisibility Cloak");
        a2.setDescription("An invisibility cloak is used to make the wearer invisible.");
        a2.setImageUrl("ImageUrl");

        Project a3 = new Project();
        a3.setId("1250808601744904193");
        a3.setName("Elder Wand");
        a3.setDescription("The Elder Wand, known throughout history as the Deathstick or the Wand of Destiny, is an extremely powerful wand made of elder wood with a core of Thestral tail hair.");
        a3.setImageUrl("ImageUrl");

        Project a4 = new Project();
        a4.setId("1250808601744904194");
        a4.setName("The Marauder's Map");
        a4.setDescription("A magical map of Hogwarts created by Remus Lupin, Peter Pettigrew, Sirius Black, and James Potter while they were students at Hogwarts.");
        a4.setImageUrl("ImageUrl");

        Project a5 = new Project();
        a5.setId("1250808601744904195");
        a5.setName("The Sword Of Gryffindor");
        a5.setDescription("A goblin-made sword adorned with large rubies on the pommel. It was once owned by Godric Gryffindor, one of the medieval founders of Hogwarts.");
        a5.setImageUrl("ImageUrl");

        Project a6 = new Project();
        a6.setId("1250808601744904196");
        a6.setName("Resurrection Stone");
        a6.setDescription("The Resurrection Stone allows the holder to bring back deceased loved ones, in a semi-physical form, and communicate with them.");
        a6.setImageUrl("ImageUrl");



        Vendor w1 = new Vendor();
        w1.setId(1);
        w1.setName("House of Armor");
        w1.addProject(a2);



        Vendor w2 = new Vendor();
        w2.setId(2);
        w2.setName("Chest of Magic Devices");
        w2.addProject(a6);
        w2.addProject(a4);



        Vendor w3 = new Vendor();
        w3.setId(3);
        w3.setName("The Tooth of The Sword");
        w3.addProject(a5);
        w3.addProject(a3);

        vendorRepository.save(w1);
        vendorRepository.save(w2);
        vendorRepository.save(w3);

        projectRepository.save(a1);

        // Create some users.
        User u1 = new User();
        u1.setId(1);
        u1.setUsername("john");
        u1.setPassword("123456");
        u1.setEnabled(true);
        u1.setRoles("admin user");

        User u2 = new User();
        u2.setId(2);
        u2.setUsername("eric");
        u2.setPassword("654321");
        u2.setEnabled(true);
        u2.setRoles("user");

        User u3 = new User();
        u3.setId(3);
        u3.setUsername("tom");
        u3.setPassword("qwerty");
        u3.setEnabled(false);
        u3.setRoles("user");

        this.userService.save(u1);
        this.userService.save(u2);
        this.userService.save(u3);


    }
}
