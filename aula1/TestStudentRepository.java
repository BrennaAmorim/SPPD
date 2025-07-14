import java.util.Date;
import java.util.List;

public class TestStudentRepository {
    public static void main(String[] args) {
        Database database = new Database("students.db");
        StudentRepository repo = new StudentRepository(database);
        System.out.println("\n--- Teste de CRUD de Students ---");

        // Limpar tabela para começar limpo
        try {
            com.j256.ormlite.table.TableUtils.clearTable(database.getConnection(), Student.class);
            System.out.println("Todos os estudantes foram deletados com sucesso.");
        } catch (Exception e) {
            System.out.println(e);
        }

        System.out.println("\n--- Criando Students ---");
        Student s1 = new Student();
        s1.setFullName("Maria Silva");
        s1.setRegistration(1001);
        s1.setBirthday(new Date());

        Student s2 = new Student();
        s2.setFullName("João Pereira");
        s2.setRegistration(1002);
        s2.setBirthday(new Date());

        repo.create(s1);
        repo.create(s2);

        System.out.println("Estudante criado: " + s1);
        System.out.println("Estudante criado: " + s2);

        System.out.println("\n--- Lendo Students ---");
        List<Student> students = repo.loadAll();
        for (Student s : students) {
            System.out.println(s);
        }

        System.out.println("\n--- Buscando Student por ID ---");
        Student found = repo.loadFromId(s1.getId());
        System.out.println("Estudante encontrado por ID: " + found);

        System.out.println("\n--- Atualizando Student ---");
        s1.setRegistration(9999);
        repo.update(s1);
        System.out.println("Estudante atualizado: " + s1);

        System.out.println("\n--- Deletando Student ---");
        repo.delete(s2);
        System.out.println("Estudante deletado: " + s2);

        System.out.println("\nEstudantes restantes após exclusão:");
        students = repo.loadAll();
        for (Student s : students) {
            System.out.println(s);
        }

        System.out.println("\n--- Teste CRUD Concluído ---");
        database.close();
    }
}
