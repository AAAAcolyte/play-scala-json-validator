package controllers

import java.util

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include
import com.fasterxml.jackson.core.JsonFactory
import com.fasterxml.jackson.core.`type`.TypeReference
import com.fasterxml.jackson.databind
import com.fasterxml.jackson.databind.deser.std.StringDeserializer
import com.fasterxml.jackson.databind.{JsonNode, ObjectMapper}
import javax.inject._
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}
import com.github.fge.jackson.JsonLoader
import com.github.fge.jsonschema.cfg.ValidationConfiguration
import com.github.fge.jsonschema.core.report.ProcessingReport
import com.github.fge.jsonschema.main.{JsonSchema, JsonSchemaFactory}
import play.api.inject.SimpleModule
import play.api.libs.json.{JsValue, Json}
/**
  * This controller handles a file upload.
  */
@Singleton
class HomeController @Inject()(cc: MessagesControllerComponents)
                              (implicit executionContext: ExecutionContext)
  extends MessagesAbstractController(cc) {
  private val schemaContentMap = new util.HashMap[String, String]()
  private var json: String = new String()

  def homepage = Action {
    Ok
  }

  def postSchema(SchemaID:String) = Action(parse.anyContent) { request =>
    val body = request.body
    val jsonBody: Option[JsValue] = body.asJson
    jsonBody.map { json =>
      schemaContentMap.put(SchemaID,jsonBody.get.toString)
      // Success Message
      Created(Json.obj("action" -> "uploadschema","id" -> SchemaID,"status" -> "success"))
    }.getOrElse {
      // Error message
      Created(Json.obj("action" -> "uploadschema","id" -> SchemaID,"status" -> "error", "message" -> "Invalide Json"))
    }
  }

  def getSchema(SchemaID:String) = Action {
    request =>
      Ok(schemaContentMap.get(SchemaID))
  }

  def validate(SchemaID:String) = Action(parse.json) { request =>
    val mapper:ObjectMapper = new databind.ObjectMapper()
    mapper.setSerializationInclusion(Include.NON_NULL)
    val content:String = request.body.toString()
    var jsonToTest = mapper.readTree(content)
    var schemaJson = mapper.readTree(schemaContentMap.get(SchemaID))
    cleanJson(jsonToTest)
    var jsonSchemaFactory = JsonSchemaFactory.byDefault()
    var schema = jsonSchemaFactory.getJsonSchema(schemaJson)
    var report = schema.validate(jsonToTest)
    if(report.isSuccess) {
      Ok(Json.obj("action" -> "validatedocument","id" -> SchemaID, "status" -> "success"))
    }else{
      Ok(Json.obj("action" -> "validatedocument","id" -> SchemaID, "status" -> "error", "message" -> report.toString))
    }
  }

  private def cleanJson(jsonNode:JsonNode): Unit ={
    val it = jsonNode.iterator
    while(it.hasNext){
      val child = it.next()
      if(child.isNull){
        it.remove()
      }else{
        cleanJson(child)
      }
    }
  }
}
