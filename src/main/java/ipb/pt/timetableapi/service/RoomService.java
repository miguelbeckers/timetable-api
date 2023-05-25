package ipb.pt.timetableapi.service;

import ipb.pt.timetableapi.model.Room;
import ipb.pt.timetableapi.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoomService {

    @Autowired
    private RoomRepository roomRepository;

    public List<Room> findAll(){
        return roomRepository.findAll();
    }

    public Optional<Room> findById(Long id){
        return roomRepository.findById(id);
    }

    public Room create(Room room){
        return roomRepository.save(room);
    }

    public Room update(Room room){
        return roomRepository.save(room);
    }

    public void delete(Room room){
        roomRepository.delete(room);
    }
}