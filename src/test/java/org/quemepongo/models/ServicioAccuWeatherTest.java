package org.quemepongo.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.quemepongo.utils.DomainException;

class ServicioAccuWeatherTest {

  private AccuWeatherAPI accuWeatherAPI;

  @BeforeEach
  public void setUp() {
    accuWeatherAPI = Mockito.mock(AccuWeatherAPI.class);
  }
  @Test
  void deberiaObtenerLaTemperaturaActual() {
    Mockito.when(accuWeatherAPI.getWeather(any())).thenReturn(dataCorrecta());

    ServicioAccuWeather servicioAccuWeather = new ServicioAccuWeather(accuWeatherAPI);

    assertEquals(57, servicioAccuWeather.getTemperaturaActual().getValor());

  }

  @Test
  void cuandoNoHayClimasDisponibles() {
    Mockito.when(accuWeatherAPI.getWeather(any())).thenReturn(List.of());

    ServicioAccuWeather servicioAccuWeather = new ServicioAccuWeather(accuWeatherAPI);

    assertThrows(DomainException.class, () -> servicioAccuWeather.getTemperaturaActual().getValor());

  }

  @Test
  void cuandoHayTemperaturasConUnidadesInvalidas() {
    Mockito.when(accuWeatherAPI.getWeather(any())).thenReturn(dataIncorrecta());

    ServicioAccuWeather servicioAccuWeather = new ServicioAccuWeather(accuWeatherAPI);

    assertThrows(IllegalArgumentException.class, () -> servicioAccuWeather.getTemperaturaActual().getValor());

  }

  private List<Map<String, Object>> dataCorrecta() {
    return Arrays.asList(new HashMap<>() {
      {
        put("Temperature", new HashMap<String, Object>() {{
          put("Value", 57);
          put("Unit", "F");
          put("UnitType", 18);
        }});
      }
    });
  }

  private List<Map<String, Object>> dataIncorrecta() {
    return Arrays.asList(new HashMap<>() {
      {
        put("Temperature", new HashMap<String, Object>() {{
          put("Value", 57);
          put("Unit", "M");
          put("UnitType", 18);
        }});
      }
    });
  }

}