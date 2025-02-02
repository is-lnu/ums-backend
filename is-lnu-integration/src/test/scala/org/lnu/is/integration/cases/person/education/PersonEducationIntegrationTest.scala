package org.lnu.is.integration.cases.person.education

import java.util.UUID
import scala.concurrent.duration.DurationInt
import io.gatling.core.Predef.checkBuilder2Check
import io.gatling.core.Predef.findCheckBuilder2ValidatorCheckBuilder
import io.gatling.core.Predef.exec
import io.gatling.core.Predef.stringToExpression
import io.gatling.core.Predef.validatorCheckBuilder2CheckBuilder
import io.gatling.core.Predef.value2Expression
import io.gatling.core.Predef.value2Success
import io.gatling.http.Predef.ELFileBody
import io.gatling.http.Predef.http
import io.gatling.http.Predef.jsonPath
import io.gatling.http.Predef.status
import org.lnu.is.integration.config.ComplexTest
import io.gatling.core.structure.ChainBuilder
import org.lnu.is.integration.config.helper.FirstName
import org.lnu.is.integration.config.helper.FatherName
import org.lnu.is.integration.config.helper.LastName
import org.lnu.is.integration.config.helper.Photo
import org.lnu.is.integration.config.helper.DocSeries
import org.lnu.is.integration.config.helper.BirthPlace
import java.util.Random

object PersonEducationIntegrationTest extends ComplexTest {
	
  val testCase = exec(init)
    .exec(before)
    .exec(http("Post Person Education")
        .post("/persons/${personId}/educations")
        .basicAuth("admin", "nimda")
        .header("Content-Type", "application/json")
        .body(ELFileBody("data/person/education/post.json"))
        .asJSON
        .check(status.is(201))
        .check(jsonPath("$.id").find.saveAs("personEducationId")))
    .exec(http("Get Person Education")
        .get("/persons/${personId}/educations/${personEducationId}")
        .basicAuth("admin", "nimda")
        .check(status.is(200)))
    .exec(http("Update Person Education")
        .put("/persons/${personId}/educations/${personEducationId}")
        .basicAuth("admin", "nimda")
        .header("Content-Type", "application/json")
        .body(ELFileBody("data/person/education/put.json"))
        .asJSON
        .check(status.is(200)))
    .exec(http("Get Person Education")
        .get("/persons/${personId}/educations/${personEducationId}")
        .basicAuth("admin", "nimda")
        .check(status.is(200))
        .check(jsonPath("$.begDate").find.is("2014-02-01"))
        .check(jsonPath("$.endDate").find.is("2015-02-01"))
        .check(jsonPath("$.educationTypeId").find.is("2"))
    )
    .exec(http("Delete Person Education")
        .delete("/persons/${personId}/educations/${personEducationId}")
        .basicAuth("admin", "nimda")
        .check(status.is(204)))
    .exec(after)  
  
  def init(): ChainBuilder = {
    exec(session => {
          session
            .set("person_idnum", UUID.randomUUID())
            .set("person_firstname", FirstName.generate())
            .set("person_fathername", FatherName.generate())
            .set("person_lastname", LastName.generate())
            .set("person_photo", Photo.generate())
            .set("person_birthplace", BirthPlace.generate())
            .set("person_docnum", new Random().nextLong())
            .set("person_docseries", DocSeries.generate())
            
            .set("docSeries", UUID.randomUUID())
            .set("docNum", UUID.randomUUID())
            .set("docDate", "2010-01-01")
            .set("docIssued", UUID.randomUUID())
            .set("docPin", UUID.randomUUID())
       })
  }
  
	def before(): ChainBuilder = {
    exec(http("Post Person")
      .post("/persons")
      .basicAuth("admin", "nimda")
      .header("Content-Type", "application/json")
      .body(ELFileBody("data/person/post.json"))
      .asJSON
      .check(status.is(201))
      .check(jsonPath("$.id").find.saveAs("personId")))
    .exec(http("Post Person Paper")
        .post("/persons/${personId}/papers")
        .basicAuth("admin", "nimda")
        .header("Content-Type", "application/json")
        .body(ELFileBody("data/person/paper/post.json"))
        .asJSON
        .check(status.is(201))
        .check(jsonPath("$.id").find.saveAs("personPaperId")))      
	}

  def after(): ChainBuilder = {
    exec(http("Delete Person")
      .delete("/persons/${personId}")
      .basicAuth("admin", "nimda")
      .check(status.is(204)))
    .exec(http("Delete Person Paper")
        .delete("/persons/${personId}/papers/${personPaperId}")
        .basicAuth("admin", "nimda")
        .check(status.is(204)))      
  }


}