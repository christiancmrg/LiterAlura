package com.aluracursos.screenmatch.services;

import java.util.List;

public interface IConvierteDatos {
    <T> T obtenerDatos(String json, Class<T> clase);
    <T> List<T> obtenerLista(String json, String nombreCampo, Class<T> clase);
    <T> T obtenerPrimeroDeLista(String json, String nombreCampo, Class<T> clase);
}
