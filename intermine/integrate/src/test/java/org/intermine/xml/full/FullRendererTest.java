package org.intermine.xml.full;

/*
 * Copyright (C) 2002-2022 FlyMine
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU Lesser General Public Licence.  This should
 * be distributed with the code.  See the LICENSE file for more
 * information or http://www.gnu.org/copyleft/lesser.html.
 *
 */

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.math.BigDecimal;
import java.util.*;

import org.custommonkey.xmlunit.XMLTestCase;
import org.custommonkey.xmlunit.XMLUnit;
import org.intermine.metadata.Model;
import org.intermine.model.FastPathObject;
import org.intermine.model.testmodel.Address;
import org.intermine.model.testmodel.Broke;
import org.intermine.model.testmodel.Company;
import org.intermine.model.testmodel.Department;
import org.intermine.model.testmodel.Employee;
import org.intermine.model.testmodel.Types;
import org.intermine.util.DynamicUtil;

public class FullRendererTest extends XMLTestCase
{
    private Model model;
    private final String ENDL = System.getProperty("line.separator");

    public void setUp() throws Exception {
        super.setUp();

        model = Model.getInstanceByName("testmodel");

        XMLUnit.setIgnoreWhitespace(true);
    }

    public void testRenderItem() throws Exception {
        Item item1 = new Item();
        item1.setImplementations("Company");
        item1.setIdentifier("1");
        Attribute attr1 = new Attribute();
        attr1.setName("name");
        attr1.setValue("Company1");
        item1.addAttribute(attr1);
        Reference ref1 = new Reference();
        ref1.setName("address");
        ref1.setRefId("2");
        item1.addReference(ref1);
        ReferenceList col1 = new ReferenceList();
        col1.setName("departments");
        col1.addRefId("3");
        col1.addRefId("4");
        item1.addCollection(col1);

        String expected = "<item id=\"1\" class=\"\" implements=\"Company\">"
            + "<attribute name=\"name\" value=\"Company1\"/>"
            + "<reference name=\"address\" ref_id=\"2\"/>"
            + "<collection name=\"departments\">"
            + "<reference ref_id=\"3\"/>"
            + "<reference ref_id=\"4\"/>"
            + "</collection>"
            + "</item>";

        String got = FullRenderer.render(item1);

        assertXMLEqual(expected, got);
    }

    public void testRenderItems() throws Exception {
        String generated = FullRenderer.render(getExampleItems());
        InputStream expected = getClass().getClassLoader().getResourceAsStream("FullParserTest.xml");

        assertXMLEqual(new InputStreamReader(expected), new StringReader(generated));
    }

    public void testToItemMaterial() {
        Employee e = new Employee();
        Department d = new Department();
        e.setId(1234);
        e.setName("Employee1");
        d.setId(5678);
        e.setDepartment(d);

        Item exp1 = new Item();
        exp1.setIdentifier("1234");
        exp1.setClassName("Employee");
        exp1.setImplementations("Employable HasAddress");
        Attribute atr1 = new Attribute();
        atr1.setName("name");
        atr1.setValue("Employee1");
        exp1.addAttribute(atr1);
        Attribute atr2 = new Attribute();
        atr2.setName("age");
        atr2.setValue("0");
        exp1.addAttribute(atr2);
        Attribute atr3 = new Attribute();
        atr3.setName("fullTime");
        atr3.setValue("false");
        exp1.addAttribute(atr3);
        Reference ref = new Reference();
        ref.setName("department");
        ref.setRefId("5678");
        exp1.addReference(ref);

        assertEquals(exp1, new ItemFactory(model).makeItem(e));
    }

    public void testToItemDynamic() {
        Department d1 = new Department();
        d1.setId(5678);
        Department d2 = new Department();
        d2.setId(6789);

        Object o = DynamicUtil.createObject(new HashSet<Class<?>>(Arrays.asList(Company.class, Broke.class)));
        Company c = (Company) o;
        c.setId(1234);
        c.setName("BrokeCompany1");
        c.setDepartments(new LinkedHashSet<Department>(Arrays.asList(d1, d2)));

        Broke b = (Broke) o;
        b.setDebt(10);

        Item exp1 = new Item();
        exp1.setIdentifier("1234");
        exp1.setClassName("");
        exp1.setImplementations("Broke Company");
        exp1.setAttribute("name", "BrokeCompany1");
        exp1.setAttribute("debt", "10");
        exp1.setAttribute("vatNumber", "0");
        exp1.setAttribute("interestRate", "0.0");
        List<String> refIds = new ArrayList<String>(Arrays.asList("5678", "6789"));
        exp1.setCollection("departments", refIds);
        assertEquals(exp1.toString(), (new ItemFactory(model).makeItem(b)).toString());
    }

    public void testToItems() {
        Address a1 = new Address();
        a1.setId(2);
        a1.setAddress("\"Company's\" street");
        Department d1 = new Department();
        d1.setId(3);
        d1.setName("Department1");
        Department d2 = new Department();
        d2.setId(4);
        d2.setName("Department2");

        Object o1 = DynamicUtil.createObject(new HashSet<Class<?>>(Collections.singletonList(Company.class)));
        Company c1 = (Company) o1;
        c1.setId(1);
        c1.setName("Company1");
        c1.setAddress(a1);
        c1.setVatNumber(10);
        c1.setDepartments(new HashSet<Department>(Arrays.asList(d1, d2)));

        List<FastPathObject> objects = Arrays.asList((FastPathObject)c1, a1, d1, d2);

        List rendered = FullRenderer.toItems(objects, model);
        assertTrue(rendered.toString(), rendered.equals(getExampleItems(true)) || rendered.equals(getExampleItems(false)));

    }

    public void testRenderObjectNoId() {
        Employee e = new Employee();
        e.setName("Employee1");

        try {
            FullRenderer.render(e, model);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // expected
        }
    }

    public void testRenderObjectMaterial() throws Exception {
        Employee e = new Employee();
        Department d = new Department();
        e.setId(1234);
        e.setName("Employee1");
        d.setId(5678);
        e.setDepartment(d);

        String expected = "<item id=\"1234\" class=\"Employee\" implements=\"Employable HasAddress\">" + ENDL
            + "<attribute name=\"age\" value=\"0\"/>" + ENDL
            + "<attribute name=\"fullTime\" value=\"false\"/>" + ENDL
            + "<attribute name=\"name\" value=\"Employee1\"/>" + ENDL
            + "<reference name=\"department\" ref_id=\"5678\"/>" + ENDL
            + "</item>" + ENDL;

        String got = FullRenderer.render(e, model);
        assertXMLEqual(expected, got);
    }

    public void testRenderObjectDynamic() throws Exception {
        Department d1 = new Department();
        d1.setId(5678);
        Department d2 = new Department();
        d2.setId(6789);

        Object o = DynamicUtil.createObject(new HashSet<Class<?>>(Arrays.asList(Company.class, Broke.class)));
        Company c = (Company) o;
        c.setId(1234);
        c.setName("BrokeCompany1");
        c.setDepartments(new LinkedHashSet<Department>(Arrays.asList(d1, d2)));

        Broke b = (Broke) o;
        b.setDebt(10);

        String expected = "<item id=\"1234\" class=\"\" implements=\"Broke Company\">" + ENDL
            + "<attribute name=\"debt\" value=\"10\"/>" + ENDL
            + "<attribute name=\"interestRate\" value=\"0.0\"/>" + ENDL
            + "<attribute name=\"name\" value=\"BrokeCompany1\"/>" + ENDL
            + "<attribute name=\"vatNumber\" value=\"0\"/>" + ENDL
            + "<collection name=\"departments\">" + ENDL
            + "<reference ref_id=\"5678\"/>" + ENDL
            + "<reference ref_id=\"6789\"/>" + ENDL
            + "</collection>" + ENDL
            + "</item>" + ENDL;

        assertXMLEqual(expected, FullRenderer.render(b, model));
    }

    public void testRenderBusinessObjects() throws Exception {
        Department d1 = new Department();
        d1.setId(5678);
        Department d2 = new Department();
        d2.setId(6789);

        List<FastPathObject> list = Arrays.asList((FastPathObject)d1, d2);

        String expected = "<items>" + ENDL
            + "<item id=\"5678\" class=\"Department\" implements=\"RandomInterface\">" + ENDL
            + "</item>" + ENDL
            + "<item id=\"6789\" class=\"Department\" implements=\"RandomInterface\">" + ENDL
            + "</item>" + ENDL
            + "</items>" + ENDL;

        assertXMLEqual(expected, FullRenderer.render(list, model));
    }

    public void testRenderTypes() throws Exception {
        Types t = new Types();
        t.setId(1234);
        t.setName("Types1");
        t.setBooleanType(true);
        t.setFloatType(1.2f);
        t.setDoubleType(1.3d);
        t.setShortType((short) 231);
        t.setIntType(2);
        t.setLongType(327641237623423L);
        t.setBooleanObjType(Boolean.TRUE);
        t.setFloatObjType(2.2f);
        t.setDoubleObjType(2.3);
        t.setShortObjType((short)786);
        t.setIntObjType(4);
        t.setLongObjType(876328471234L);
        t.setBigDecimalObjType(new BigDecimal("9872876349183274123432.876128716235487621432"));
        t.setDateObjType(new Date(7777777777L));
        t.setStringObjType("A String");

        String expected = "<item id=\"1234\" class=\"Types\">" + ENDL
            + "<attribute name=\"bigDecimalObjType\" value=\"9872876349183274123432.876128716235487621432\"/>" + ENDL
            + "<attribute name=\"booleanObjType\" value=\"true\"/>" + ENDL
            + "<attribute name=\"booleanType\" value=\"true\"/>" + ENDL
            + "<attribute name=\"dateObjType\" value=\"7777777777\"/>" + ENDL
            + "<attribute name=\"doubleObjType\" value=\"2.3\"/>" + ENDL
            + "<attribute name=\"doubleType\" value=\"1.3\"/>" + ENDL
            + "<attribute name=\"floatObjType\" value=\"2.2\"/>" + ENDL
            + "<attribute name=\"floatType\" value=\"1.2\"/>" + ENDL
            + "<attribute name=\"intObjType\" value=\"4\"/>" + ENDL
            + "<attribute name=\"intType\" value=\"2\"/>" + ENDL
            + "<attribute name=\"longObjType\" value=\"876328471234\"/>" + ENDL
            + "<attribute name=\"longType\" value=\"327641237623423\"/>" + ENDL
            + "<attribute name=\"name\" value=\"Types1\"/>" + ENDL
            + "<attribute name=\"shortObjType\" value=\"786\"/>" + ENDL
            + "<attribute name=\"shortType\" value=\"231\"/>" + ENDL
            + "<attribute name=\"stringObjType\" value=\"A String\"/>" + ENDL
            + "</item>" + ENDL;

        assertXMLEqual(expected, FullRenderer.render(t, model));
    }

    private List<Item> getExampleItems() {
        return getExampleItems(true);
    }

    private List<Item> getExampleItems(boolean reverseCollection) {
        List<Item> exampleItems = new ArrayList<Item>();

        String id1 = "1";
        String id2 = "2";
        String id3 = "3";
        String id4 = "4";

        Item item1 = new Item();
        item1.setImplementations("Company");
        item1.setIdentifier(id1);
        Attribute attr1 = new Attribute();
        attr1.setName("name");
        attr1.setValue("Company1");
        item1.addAttribute(attr1);
        Attribute attr2 = new Attribute();
        attr2.setName("vatNumber");
        attr2.setValue("10");
        item1.addAttribute(attr2);

        Reference ref1 = new Reference();
        ref1.setName("address");
        ref1.setRefId(id2);
        item1.addReference(ref1);
        ReferenceList col1 = new ReferenceList();
        col1.setName("departments");
        if (reverseCollection) {
            col1.addRefId(id3);
        }
        col1.addRefId(id4);
        if (! reverseCollection) {
            col1.addRefId(id3);
        }
        item1.addCollection(col1);

        Item item2 = new Item();
        item2.setClassName("Address");
        item2.setImplementations("Thing");
        item2.setIdentifier(id2);
        Attribute attr3 = new Attribute();
        attr3.setName("address");
        attr3.setValue("\"Company's\" street");
        item2.addAttribute(attr3);

        Item item3 = new Item();
        item3.setClassName("Department");
        item3.setImplementations("RandomInterface");
        item3.setIdentifier(id3);
        Attribute attr4 = new Attribute();
        attr4.setName("name");
        attr4.setValue("Department1");
        item3.addAttribute(attr4);

        Item item4 = new Item();
        item4.setClassName("Department");
        item4.setImplementations("RandomInterface");
        item4.setIdentifier(id4);
        Attribute attr5 = new Attribute();
        attr5.setName("name");
        attr5.setValue("Department2");
        item4.addAttribute(attr5);

        exampleItems.add(item1);
        exampleItems.add(item2);
        exampleItems.add(item3);
        exampleItems.add(item4);

        return exampleItems;
    }
}
