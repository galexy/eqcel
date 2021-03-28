import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;

import java.io.InputStreamReader;

import collection.JavaConverters._
import com.google.api.services.sheets.v4.model.Spreadsheet
import com.google.api.services.sheets.v4.model.SpreadsheetProperties
import com.google.api.services.sheets.v4.model.BatchUpdateValuesRequest

import net.reasoning.eqcel.intermediate.ExpandedSheet
import net.reasoning.eqcel.intermediate.EmptyCell
import net.reasoning.eqcel.intermediate.IntCell
import net.reasoning.eqcel.intermediate.FormulaCell

class GoogleSheetCompiler {
  def getSheetsService(): Sheets = {
    def getCredentials(httpTransport: NetHttpTransport, jsonFactory: JsonFactory): Credential = {
      val tokensDirectoryPath: String = "tokens";
      val scopes = List(SheetsScopes.SPREADSHEETS)
      val credentialsFilePath = "/credentials.json";

      val in = QuickStart.getClass().getResourceAsStream(credentialsFilePath)
      val clientSecrets = GoogleClientSecrets.load(jsonFactory, new InputStreamReader(in))

      val flow = new GoogleAuthorizationCodeFlow
        .Builder(httpTransport, jsonFactory, clientSecrets, scopes.asJava)
        .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(tokensDirectoryPath)))
        .setAccessType("offline")
        .build()

      val receiver = new LocalServerReceiver.Builder().setPort(8888).build()
      new AuthorizationCodeInstalledApp(flow, receiver).authorize("user")
    }

    val applicationName: String = "EqCel";
    val jsonFactory: JsonFactory = JacksonFactory.getDefaultInstance()

    val httpTransport = GoogleNetHttpTransport.newTrustedTransport()
    val credentials = getCredentials(httpTransport, jsonFactory)

    new Sheets.Builder(httpTransport, jsonFactory, credentials)
      .setApplicationName(applicationName)
      .build()
  }

  def build(expanded: ExpandedSheet, title: String) {
    val sheets = getSheetsService()
    val spreadsheetDef = new Spreadsheet()
      .setProperties(new SpreadsheetProperties().setTitle(title))

    val spreadsheet = sheets.spreadsheets().create(spreadsheetDef)
      .setFields("spreadsheetId")
      .execute()

    val spreadsheetId = spreadsheet.getSpreadsheetId()

    val data = expanded.ranges.map(r => {
      val values = List(r.cells.map { _ match {
        case EmptyCell => null
        case IntCell(value) => value
        case FormulaCell(formula) => formula
      }}.asJava).asJava

      new ValueRange().setRange(r.name).setValues(values)
    }).asJava

    val body = new BatchUpdateValuesRequest()
      .setValueInputOption("USER_ENTERED")
      .setData(data)
    val result = sheets.spreadsheets().values.batchUpdate(spreadsheetId, body).execute()

    println(spreadsheetId)
    println(s"${result.getTotalUpdatedCells()} cells updated.")
  }
}
