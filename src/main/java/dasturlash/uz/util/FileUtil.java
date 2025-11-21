package dasturlash.uz.util;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class FileUtil {
    private static final ObjectMapper objectMapper;

    static {
        objectMapper = JsonMapper.builder().enable(MapperFeature.PROPAGATE_TRANSIENT_MARKER).build();
    }

    public static <T> void write(String path, T t) throws IOException {
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(path), t);
    }

    public static <T> List<T> read(String path, Class<T> clazz) {
        try {
            return objectMapper.readValue(path, objectMapper.getTypeFactory().constructCollectionType(List.class, clazz));
        } catch (IOException e) {
        throw new RuntimeException("Failed to parse JSON", e);
        }
    }
}