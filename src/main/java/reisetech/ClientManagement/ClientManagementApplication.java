package reisetech.ClientManagement;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class ClientManagementApplication {

  @Autowired
  private ClientRepository repository;

  public static void main(String[] args) {
    SpringApplication.run(ClientManagementApplication.class, args);
  }

  @GetMapping("/client")
  public Client readClient(@RequestParam String name) {
    Client client = repository.selectByName(name);
    return client;
  }

  @GetMapping("/clientList")
  public List<Client> readAllClients() {
    return repository.selectAllClients();
  }

  @PostMapping("/client")
  public void createClient(String name, int age) {
    repository.createClient(name, age);
  }

  @PatchMapping("/client")
  public void updateClient(String name, int age) {
    repository.updateClient(name, age);
  }

  @DeleteMapping("/client")
  public void deleteClient(String name) {
    repository.deleteClient(name);

  }

}
