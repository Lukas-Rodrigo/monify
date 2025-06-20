package lucastexiera.com.mschatbotopenai.integrationtests.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.specification.RequestSpecification;
import lucastexiera.com.mschatbotopenai.config.TestConfigs;
import lucastexiera.com.mschatbotopenai.dto.userwhatsapp.ChatbotMessage;
import lucastexiera.com.mschatbotopenai.dto.userwhatsapp.WhatsappUserMessageResponse;
import lucastexiera.com.mschatbotopenai.integrationtests.testcontainers.AbstractIntegrationTest;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OpenAiControllerTest extends AbstractIntegrationTest {

  private static RequestSpecification specification;
  private static ObjectMapper mapper;
  private static WhatsappUserMessageResponse normalMessageWhatsResponse;
  private static WhatsappUserMessageResponse toolMessageWhatsResponse;

  @BeforeAll
  public static void setup() {
    mapper = new ObjectMapper();
    mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    specification = new RequestSpecBuilder()
            .setBasePath("v1/chatbot/message/whatsapp")
            .setPort(TestConfigs.SERVER_PORT)
            .addFilter(new RequestLoggingFilter(LogDetail.ALL))
            .build();

    normalMessageWhatsResponse = new WhatsappUserMessageResponse("Ola, poderia me falar o que voce faz??"
            , "557581069197");
    toolMessageWhatsResponse = new WhatsappUserMessageResponse("Cadastrar compra no mercado no valor de 25,00 na categoria alimentacao"
            , "557581069197");
  }

  @Test
  @Order(1)
  void testIntegrationGivenUserMessage_when_ShouldReturnPleasantMessageToUser() throws JsonProcessingException {
    var contentBody = given().spec(specification)
            .contentType(TestConfigs.CONTENT_TYPE_JSON)
            .body(normalMessageWhatsResponse)
            .when()
            .post()
            .then()
            .statusCode(200)
            .extract()
            .body()
            .asString();

    ChatbotMessage chatbotMessage = mapper.readValue(contentBody, ChatbotMessage.class);
    assertNotNull(chatbotMessage);
    System.out.println(chatbotMessage.message());
  }

  @Test
  @Order(2)
  void testIntegrationGivenUserToolMessage_when_ShouldConfirmationMessageToUser() throws JsonProcessingException {
    var contentBody = given().spec(specification)
            .contentType(TestConfigs.CONTENT_TYPE_JSON)
            .body(toolMessageWhatsResponse)
            .when()
            .post()
            .then()
            .statusCode(200)
            .extract()
            .body()
            .asString();

    ChatbotMessage chatbotMessage = mapper.readValue(contentBody, ChatbotMessage.class);

    assertNotNull(chatbotMessage);
    System.out.println(chatbotMessage.message());
  }

  @Test
  @Order(3)
  void testIntegrationGivenNewCategory_when_ShouldConfirmationMessageToUser() throws JsonProcessingException {
    var newCategoryUserMessage = new WhatsappUserMessageResponse("Cadastrar casamento como uma nova categoria"
            , "557581069197");
    var contentBody = given().spec(specification)
            .contentType(TestConfigs.CONTENT_TYPE_JSON)
            .body(newCategoryUserMessage)
            .when()
            .post()
            .then()
            .statusCode(200)
            .extract()
            .body()
            .asString();

    ChatbotMessage chatbotMessage = mapper.readValue(contentBody, ChatbotMessage.class);
    assertNotNull(chatbotMessage);
    assertTrue(chatbotMessage.message().contains("Casamento"));
    System.out.println(chatbotMessage.message());
  }
}
