package ej.documentgeneratorapi.domain;

import org.custommonkey.xmlunit.XMLTestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.io.ClassPathResource;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static ej.documentgeneratorapi.domain.CarTest.createCar;
import static org.custommonkey.xmlunit.XMLUnit.setIgnoreWhitespace;

@RunWith(MockitoJUnitRunner.class)
public class InvoiceTest extends XMLTestCase {

    private final static String CUSTOMER_NO = "CUSTOMER_NO";
    private final static String INVOICE_NO = "INVOICE_NO";
    private final static String VAT_ID_NO = "VAT_ID_NO";
    private final static String DATE = "07.07.2020";
    private final static List<Car> CARS = Arrays.asList(createCar(), createCar());

    private Marshaller marshaller;

    @Before
    public void setup() throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(Invoice.class);
        marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        setIgnoreWhitespace(true);
    }

    @Test
    public void mapInvoice_Data_Invoice() {
        Invoice invoice = createInvoice();
        assertEquals(CUSTOMER_NO, invoice.getCustomerNo());
        assertEquals(INVOICE_NO, invoice.getInvoiceNo());
        assertEquals(VAT_ID_NO, invoice.getVatIdNo());
        assertEquals(DATE, invoice.getDate());
        assertEquals("2", invoice.getCount());
        assertEquals("0,00", invoice.getAmountVat());
        assertEquals("20", invoice.getAmountWithoutVat());
        assertEquals("20", invoice.getAmountWithVat());
    }

    @Test
    public void mapInvoiceWithoutCars_Data_Invoice() {
        Invoice invoice = createInvoiceWithoutCars();
        assertEquals(CUSTOMER_NO, invoice.getCustomerNo());
        assertEquals(INVOICE_NO, invoice.getInvoiceNo());
        assertEquals(VAT_ID_NO, invoice.getVatIdNo());
        assertEquals(DATE, invoice.getDate());
        assertEquals("0", invoice.getCount());
        assertEquals("0,00", invoice.getAmountVat());
        assertEquals("0", invoice.getAmountWithoutVat());
        assertEquals("0", invoice.getAmountWithVat());
    }

    @Test
    public void parseObjectToXml_Data_Xml() throws JAXBException, IOException, SAXException {
        File file = new ClassPathResource("testInvoice.xml").getFile();
        String expectXml = new String(Files.readAllBytes(file.toPath()));
        StringWriter actualXml = new StringWriter();
        Invoice invoice = createInvoice();

        marshaller.marshal(invoice, actualXml);

        assertXMLEqual(expectXml, actualXml.toString());
    }

    private Invoice createInvoice() {
        Invoice invoice = new Invoice();
        invoice.setCustomerNo(CUSTOMER_NO);
        invoice.setDate(DATE);
        invoice.setInvoiceNo(INVOICE_NO);
        invoice.setVatIdNo(VAT_ID_NO);
        invoice.setCars(CARS);
        return invoice;
    }

    private Invoice createInvoiceWithoutCars() {
        Invoice invoice = new Invoice();
        invoice.setCustomerNo(CUSTOMER_NO);
        invoice.setDate(DATE);
        invoice.setInvoiceNo(INVOICE_NO);
        invoice.setVatIdNo(VAT_ID_NO);
        invoice.setCars(new ArrayList<>());
        return invoice;
    }

}
