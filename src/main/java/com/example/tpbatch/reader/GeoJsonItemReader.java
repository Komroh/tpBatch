package com.example.tpbatch.reader;

import com.example.tpbatch.entity.Commune;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jspecify.annotations.Nullable;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.geojson.GeoJsonReader;
import org.springframework.batch.infrastructure.item.ItemReader;
import org.springframework.beans.factory.annotation.Value;


import java.io.IOException;
import java.nio.file.Path;

public class GeoJsonItemReader implements ItemReader<Commune> {

    private final ObjectMapper mapper = new ObjectMapper();
    private final JsonParser parser;
    private final GeoJsonReader geometryReader = new GeoJsonReader();
    public GeoJsonItemReader(
            @Value("#{jobParameters['filePath']}") String filePath) throws IOException {

        JsonFactory factory = mapper.getFactory();
        parser = factory.createParser(Path.of(filePath).toFile());

        moveToFeaturesArray();
    }

    private void moveToFeaturesArray() throws IOException {

        while (!parser.isClosed()) {

            JsonToken token = parser.nextToken();

            if (token == JsonToken.FIELD_NAME
                    && "features".equals(parser.currentName())) {

                parser.nextToken();
                return;
            }
        }
    }


    @Override
    public @Nullable Commune read() throws Exception {
        JsonToken token = parser.nextToken();

        if (token == JsonToken.END_ARRAY) {
            parser.close();
            return null;
        }

        if (token != JsonToken.START_OBJECT) {
            return read();
        }

        JsonNode feature = mapper.readTree(parser);
        JsonNode properties = feature.get("properties");
        JsonNode geometryNode = feature.get("geometry");

        Commune commune = new Commune();

        commune.setCode(properties.get("code").asText());
        commune.setNom(properties.get("nom").asText());
        commune.setDepartement(properties.get("departement").asText());
        commune.setRegion(properties.get("region").asText());
        commune.setEpci(properties.path("epci").asText(null));

        Geometry geometry = geometryReader.read(mapper.writeValueAsString(geometryNode));

        commune.setGeom(geometry);

        return commune;
    }
}
