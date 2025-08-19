package com.aluracursos.screenmatch.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConvierteDatos implements IConvierteDatos {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public <T> T obtenerDatos(String json, Class<T> clase) {
        try {
            return mapper.readValue(json, clase);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error al convertir JSON a " + clase.getSimpleName(), e);
        }
    }

    @Override
    public <T> List<T> obtenerLista(String json, String nombreCampo, Class<T> clase) {
        try {
            JsonNode root = mapper.readTree(json);
            JsonNode nodo = root.get(nombreCampo);
            if (nodo == null || !nodo.isArray() || nodo.size() == 0) {
                throw new RuntimeException("No se encontró el campo \"" + nombreCampo + "\" o está vacío.");
            }
            var tipoLista = mapper.getTypeFactory().constructCollectionType(List.class, clase);
            return mapper.convertValue(nodo, tipoLista);
        } catch (Exception e) {
            throw new RuntimeException("Error al convertir \"" + nombreCampo + "\" a lista de " + clase.getSimpleName(), e);
        }
    }

    @Override
    public <T> T obtenerPrimeroDeLista(String json, String nombreCampo, Class<T> clase) {
        try {
            JsonNode root = mapper.readTree(json);
            JsonNode nodo = root.get(nombreCampo);
            if (nodo == null || !nodo.isArray() || nodo.size() == 0) {
                throw new RuntimeException("No se encontró el campo \"" + nombreCampo + "\" o está vacío.");
            }
            return mapper.treeToValue(nodo.get(0), clase);
        } catch (Exception e) {
            throw new RuntimeException("Error al tomar primer elemento de \"" + nombreCampo + "\"", e);
        }
    }
}
