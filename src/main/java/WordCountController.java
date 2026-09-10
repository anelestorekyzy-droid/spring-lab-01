package kz.iitu.springlab;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class WordCountController {

    @GetMapping("/wordcount")
    public WordCountResult wordCount(
            @RequestParam(defaultValue = "") String text) {

        String trimmed = text.trim();

        if (trimmed.isEmpty()) {
            return new WordCountResult(0, 0, "");
        }

        String[] words = trimmed.split("\\s+");

        String longestWord = words[0];

        for (String word : words) {
            if (word.length() > longestWord.length()) {
                longestWord = word;
            }
        }

        return new WordCountResult(
                words.length,
                text.length(),
                longestWord
        );
    }

    public record WordCountResult(
            int wordCount,
            int charCount,
            String longestWord
    ) {
    }
}