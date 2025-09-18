public interface IAppointmentService {

    Appointment save(Appointment appointment);

    List<Appointment> getAll();

    Appointment getById(int id);

    Appointment update(Appointment appointment);

    void delete(int id);
}
