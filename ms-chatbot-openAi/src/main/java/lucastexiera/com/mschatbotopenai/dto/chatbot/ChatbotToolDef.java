package lucastexiera.com.mschatbotopenai.dto.chatbot;

import java.util.List;
import java.util.Map;

public enum ChatbotToolDef {
  ENVIAR_DESPESA(
          "enviar_despesa",
          "Envia os dados formatados da despesa para o backend de acordo com a categoria selecionada",
          List.of("description", "amount", "category_id"),
          Map.of(
                  "description", new OpenAiMessageRequest.Property("string", "Descrição da despesa"),
                  "amount",      new OpenAiMessageRequest.Property("number", "Valor da despesa"),
                  "category_id", new OpenAiMessageRequest.Property("number", "Identificador da categoria")
          )
  ),
  UPDATE_LAST(
          "update_last_category",
          "Envia os dados formatados da despesa para o backend atualizar de acordo com a categoria selecionada",
          List.of("description", "amount", "category_id"),
          Map.of(
                  "description", new OpenAiMessageRequest.Property("string", "Descrição da despesa"),
                  "amount",      new OpenAiMessageRequest.Property("number", "Valor da despesa"),
                  "category_id", new OpenAiMessageRequest.Property("number", "Identificador da categoria")
          )
  ),
  CREATE_CATEGORY(
          "create_category",
          "Cadastra uma nova categoria enviando seu nome para o backend",
          List.of("name"),
          Map.of(
                  "name", new OpenAiMessageRequest.Property("string", "Nome da nova categoria")
          )
  ),
  DELETE_CATEGORY(
          "delete_category",
          "Exclui uma categoria quando o usuário desejar, enviando o nome e ID da categoria para o backend",
          List.of("category_id", "category_name"),
          Map.of(
                  "category_id",   new OpenAiMessageRequest.Property("string", "ID único da categoria"),
                  "category_name", new OpenAiMessageRequest.Property("string", "Nome da categoria a ser excluída")
          )
  );

  public final String name;
  public final String description;
  public final List<String> required;
  public final Map<String, OpenAiMessageRequest.Property> properties;

  ChatbotToolDef(String name,
                 String description,
                 List<String> required,
                 Map<String, OpenAiMessageRequest.Property> properties) {
    this.name = name;
    this.description = description;
    this.required = List.copyOf(required);
    this.properties = Map.copyOf(properties);
  }
}
