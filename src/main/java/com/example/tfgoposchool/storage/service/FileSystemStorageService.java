package com.example.tfgoposchool.storage.service;

import com.example.tfgoposchool.storage.StorageController;
import com.example.tfgoposchool.storage.exceptions.StorageBadRequestException;
import com.example.tfgoposchool.storage.exceptions.StorageInternalException;
import com.example.tfgoposchool.storage.exceptions.StorageNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

@Service
public class FileSystemStorageService implements StorageService{

    private final Path rootLocation;


    public FileSystemStorageService(@Value("${upload.root-location}") String path) {
        this.rootLocation = Paths.get(path);
    }

    /**
     * Método que almacena un fichero en el almacenamiento secundario
     *
     * @param file fichero a almacenar
     * @return nombre del fichero almacenado
     * @throws StorageBadRequestException si el fichero está vacío
     * @throws StorageInternalException   si hay un error al almacenar el fichero
     * @throws StorageBadRequestException si el fichero contiene caracteres no permitidos
     */
    @Override
    public String store(MultipartFile file) {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        String extension = StringUtils.getFilenameExtension(filename);
        String justFilename = filename.replace("." + extension, "");
        // Nombre del fichero almacenado aleatorio para evitar duplicados y sin espacios
        String storedFilename = System.currentTimeMillis() + "_" + justFilename.replaceAll("\\s+", "") + "." + extension;

        try {
            if (file.isEmpty()) {
                throw new StorageBadRequestException("Fichero vacío " + filename);
            }
            if (filename.contains("..")) {
                // This is a security check
                throw new StorageBadRequestException(
                        "No se puede almacenar un fichero con una ruta relativa fuera del directorio actual "
                                + filename);
            }

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, this.rootLocation.resolve(storedFilename),
                        StandardCopyOption.REPLACE_EXISTING);
                return storedFilename;
            }

        } catch (IOException e) {
            throw new StorageInternalException("Fallo al almacenar fichero " + filename + " " + e);
        }

    }

    /**
     * Método que devuelve la ruta de todos los ficheros que hay
     * en el almacenamiento secundario del proyecto.
     */
    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.rootLocation, 1)
                    .filter(path -> !path.equals(this.rootLocation))
                    .map(this.rootLocation::relativize);
        } catch (IOException e) {
            throw new StorageInternalException("Fallo al leer ficheros almacenados " + e);
        }

    }

    /**
     * Método que es capaz de cargar un fichero a partir de su nombre
     * Devuelve un objeto de tipo Path
     */
    @Override
    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }


    /**
     * Método que es capaz de cargar un fichero a partir de su nombre
     * Devuelve un objeto de tipo Resource
     */
    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new StorageNotFoundException("No se puede leer fichero: " + filename);
            }
        } catch (MalformedURLException e) {
            throw new StorageNotFoundException("No se puede leer fichero: " + filename + " " + e);
        }
    }


    /**
     * Método que elimina todos los ficheros del almacenamiento
     * secundario del proyecto.
     */
    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }


    /**
     * Método que inicializa el almacenamiento secundario del proyecto
     */
    @Override
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new StorageInternalException("No se puede inicializar el almacenamiento " + e);
        }
    }


    @Override
    public void delete(String filename) {
        String justFilename = StringUtils.getFilename(filename);
        try {
            Path file = load(justFilename);
            Files.deleteIfExists(file);
        } catch (IOException e) {
            throw new StorageInternalException("No se puede eliminar el fichero " + filename + " " + e);
        }

    }

    /**
     * Método que devuelve la URL de un fichero a partir de su nombre
     * Devuelve un objeto de tipo String
     */
    @Override
    public String getUrl(String filename) {
        return MvcUriComponentsBuilder
                // El segundo argumento es necesario solo cuando queremos obtener la imagen
                // En este caso tan solo necesitamos obtener la URL
                .fromMethodName(StorageController.class, "serveFile", filename, null)
                .build().toUriString();
    }
}
