package com.sohail.my_springboot_server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notes")
public class NoteController {

    private static final Logger LOGGER = LoggerFactory.getLogger(NoteController.class);

    private final NoteRepository noteRepository;

    public NoteController(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    @GetMapping
    public ResponseEntity<?> getAllNotes() {
        try {
            List<Note> notes = noteRepository.findAllByOrderByCreatedAtDesc();
            return ResponseEntity.ok(notes);
        } catch (Exception ex) {
            LOGGER.error("Failed to fetch notes", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Server error"));
        }
    }

    @PostMapping
    public ResponseEntity<?> addNote(@RequestBody NoteRequest request) {
        try {
            Note note = new Note();
            note.setTitle(request.getTitle());
            note.setContent(request.getContent());

            Note savedNote = noteRepository.save(note);
            return ResponseEntity.ok(savedNote);
        } catch (Exception ex) {
            LOGGER.error("Failed to create note", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Server error"));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNote(@PathVariable Long id) {
        try {
            if (noteRepository.existsById(id)) {
                noteRepository.deleteById(id);
            }
            return ResponseEntity.ok(Map.of("message", "Note deleted successfully"));
        } catch (Exception ex) {
            LOGGER.error("Failed to delete note with id {}", id, ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Server error"));
        }
    }
}
