package com.edu.asistenteCupos.domain.prompt;

import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.MessageType;
import org.springframework.ai.chat.prompt.DefaultChatOptions;
import org.springframework.ai.chat.prompt.Prompt;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class PromptPrinterTest {
  @Test
  void imprimePromptEnModoCompleto() {
    var user = mensaje("Hola desde usuario", MessageType.USER);
    var system = mensaje("Mensaje para sistema", MessageType.SYSTEM);
    var prompt = crearPrompt(List.of(system, user));

    String output = PromptPrinter.imprimir(prompt, false);

    assertThat(output).contains("[SYSTEM] Mensaje para sistema");
    assertThat(output).contains("[USER] Hola desde usuario");
  }

  @Test
  void imprimePromptEnModoAgrupado() {
    var assistant = mensaje("Soy el modelo", MessageType.ASSISTANT);
    var user = mensaje("Soy un user", MessageType.USER);
    var prompt = crearPrompt(List.of(user, assistant));

    String output = PromptPrinter.imprimir(prompt, true);

    assertThat(output).contains("### USER MESSAGES ###");
    assertThat(output).contains("Soy un user");
    assertThat(output).contains("### ASSISTANT MESSAGES ###");
    assertThat(output).contains("Soy el modelo");
  }

  @Test
  void fallbackAlTipoUserSiFaltaMessageType() {
    var sinTipo = new FakeMessage("Texto sin tipo", null);
    var prompt = crearPrompt(List.of(sinTipo));

    String output = PromptPrinter.imprimir(prompt, true);

    assertThat(output).contains("### USER MESSAGES ###");
    assertThat(output).contains("Texto sin tipo");
  }

  @Test
  void interpretaCorrectamenteTipoComoString() {
    var tipoComoString = new FakeMessage("Texto string como tipo", null,
      Map.of("messageType", "SYSTEM"));
    var prompt = crearPrompt(List.of(tipoComoString));

    String output = PromptPrinter.imprimir(prompt, false);

    assertThat(output).contains("[SYSTEM] Texto string como tipo");
  }

  private FakeMessage mensaje(String texto, Object tipo) {
    MessageType resolvedType = tipo instanceof String s ? MessageType.valueOf(
      s) : (MessageType) tipo;
    return new FakeMessage(texto, resolvedType);
  }

  private FakePrompt crearPrompt(List<Message> mensajes) {
    DefaultChatOptions opciones = new DefaultChatOptions();
    opciones.setMaxTokens(300);
    opciones.setStopSequences(List.of("###"));
    return new FakePrompt(mensajes, opciones);
  }

  static class FakeMessage implements Message {
    private final String texto;
    private final MessageType tipo;
    private final Map<String, Object> metadata;

    public FakeMessage(String texto, MessageType tipo) {
      this(texto, tipo, tipo != null ? Map.of("messageType", tipo) : Map.of());
    }

    public FakeMessage(String texto, MessageType tipo, Map<String, Object> metadata) {

      this.texto = texto;
      this.tipo = tipo;
      this.metadata = metadata;
    }

    @Override
    public String getText() {
      return texto;
    }

    @Override
    public Map<String, Object> getMetadata() {
      return metadata;
    }

    @Override
    public MessageType getMessageType() {
      return tipo != null ? tipo : MessageType.USER;
    }
  }

  static class FakePrompt extends Prompt {
    private final List<Message> instrucciones;
    private final DefaultChatOptions opciones;

    public FakePrompt(List<Message> instrucciones, DefaultChatOptions opciones) {
      super("");
      this.instrucciones = instrucciones;
      this.opciones = opciones;
    }

    @Override
    public DefaultChatOptions getOptions() {
      return opciones;
    }

    @Override
    public List<Message> getInstructions() {
      return instrucciones;
    }
  }
}
