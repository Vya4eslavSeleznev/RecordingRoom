package main.service;

import main.entity.Instrument;
import main.entity.Room;
import main.entity.RoomInstrument;
import main.exception.EntityNotFoundException;
import main.repository.RoomInstrumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import main.repository.InstrumentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InstrumentServiceImpl implements InstrumentService {
    @Autowired
    private InstrumentRepository instrumentRepository;

    @Autowired
    private RoomInstrumentRepository roomInstrumentRepository;

    @Override
    public void add(Instrument customer) {
        instrumentRepository.save(customer);
    }

    @Override
    public void delete(long id) {
        Optional<Instrument> instrument = instrumentRepository.findById(id);
        if (!instrument.isPresent()) {
            throw new EntityNotFoundException("Instrument not found");
        }

        ((List<RoomInstrument>) roomInstrumentRepository.findAll())
          .stream()
          .filter(roomInstrument -> roomInstrument.getInstrument().getId().compareTo(id) == 0)
          .forEach(roomInstrument -> roomInstrumentRepository.delete(roomInstrument));

        instrumentRepository.delete(instrument.get());
    }

    @Override
    public Instrument getById(long id) {
        Optional<Instrument> instrument = instrumentRepository.findById(id);
        if (!instrument.isPresent()) {
            throw new EntityNotFoundException("Instrument not found");
        }
        return instrument.get();
    }

    @Override
    public boolean checkById(long id) {
        return instrumentRepository.findById(id).isPresent();
    }

    @Override
    public List<Instrument> getAll() {
        return (List<Instrument>) instrumentRepository.findAll();
    }

}
