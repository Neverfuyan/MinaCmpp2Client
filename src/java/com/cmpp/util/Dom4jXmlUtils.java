package com.cmpp.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.CharArrayReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.xml.sax.InputSource;

public class Dom4jXmlUtils
{
  public static Document load(InputStream is)
    throws Exception
  {
    SAXReader reader = new SAXReader();
    return reader.read(is);
  }

  public static Document load(String xmlfile)
    throws Exception
  {
    BufferedReader br = new BufferedReader(new FileReader(xmlfile));

    String s = null;
    String domContent = "";
    while ((s = br.readLine()) != null) {
      domContent = domContent + s;
    }
    System.out.println("\r\ndomContent:" + domContent);
    br.close();

    return loadString(domContent);
  }

  public static Document loadStringWithoutTitle(String domContent)
    throws Exception
  {
    domContent = "<?xml version=\"1.0\" encoding=\"gb2312\"?>" + domContent;
    return loadString(domContent);
  }

  public static Document loadString(String domContent)
    throws Exception
  {
    SAXReader reader = new SAXReader();
    char[] chars = new char[domContent.length()];
    domContent.getChars(0, domContent.length(), chars, 0);
    InputSource is = new InputSource(new CharArrayReader(chars));
    return reader.read(is);
  }

  public static void save(String xmlfile, Document dom) throws Exception {
    BufferedWriter bw = new BufferedWriter(new FileWriter(xmlfile));
    bw.write("<?xml version=\"1.0\" encoding=\"gb2312\"?>");
    if (dom == null) {
      bw.close();
      return;
    }
    Object el = dom.getDocument();
    if (el == null) {
      bw.close();
      return;
    }
    bw.write(el.toString());
    bw.close();
  }

  public static Document blankDocument()
    throws Exception
  {
    DocumentFactory factory = DocumentFactory.getInstance();

    return factory.createDocument();
  }

  public static String getChildText(Element parent, String name) {
    Element e = getChildByName(parent, name);
    if (e == null) {
      return "";
    }
    return getText(e);
  }

  public static String getText(Element e) {
    return e.getText();
  }

  public static Element getChildByName(Element e, String name) {
    return e.element(name);
  }

  @SuppressWarnings({ "rawtypes", "unchecked" })
public static Map getProperties(Element root) {
    Map map = new HashMap();
    Element[] list = (Element[])root.elements("property").toArray(new Element[0]);

    for (int i = 0; i < list.length; i++) {
      String name = list[i].attributeValue("name");
      String type = list[i].attributeValue("type");
      String valueString = getText(list[i]);
      try {
        Class cls = Class.forName(type);
        Constructor con = cls.getConstructor(new Class[] { String.class });
        Object value = con.newInstance(new Object[] { valueString });
        map.put(name, value);
      } catch (Exception e) {
        System.err.println("Unable to parse property '" + name + "'='" + valueString + "': " + e.toString());
      }
    }

    return map;
  }

  @SuppressWarnings({ "rawtypes", "unchecked" })
public static String[] splitOnWhitespace(String source) {
    int pos = -1;
    LinkedList list = new LinkedList();
    int max = source.length();
    for (int i = 0; i < max; i++) {
      char c = source.charAt(i);
      if (Character.isWhitespace(c)) {
        if (i - pos > 1) {
          list.add(source.substring(pos + 1, i));
        }
        pos = i;
      }
    }
    return (String[])list.toArray(new String[list.size()]);
  }

  @SuppressWarnings("rawtypes")
public static void applyProperties(Object o, Element root) {
    Map map = getProperties(root);
    Iterator it = map.keySet().iterator();
    Field[] fields = o.getClass().getFields();
    Method[] methods = o.getClass().getMethods();
    while (it.hasNext()) {
      String name = (String)it.next();
      Object value = map.get(name);
      try {
        for (int i = 0; i < fields.length; i++) {
          if ((fields[i].getName().equalsIgnoreCase(name)) && (isTypeMatch(fields[i].getType(), value.getClass())))
          {
            fields[i].set(o, value);
            System.err.println("Set field " + fields[i].getName() + "=" + value);

            break;
          }
        }
        for (int i = 0; i < methods.length; i++)
          if ((methods[i].getName().equalsIgnoreCase("set" + name)) && (methods[i].getParameterTypes().length == 1) && (isTypeMatch(methods[i].getParameterTypes()[0], value.getClass())))
          {
            methods[i].invoke(o, new Object[] { value });
            System.err.println("Set method " + methods[i].getName() + "=" + value);

            break;
          }
      }
      catch (Exception e) {
        System.err.println("Unable to apply property '" + name + "': " + e.toString());
      }
    }
  }

  @SuppressWarnings("rawtypes")
  private static boolean isTypeMatch(Class one, Class two)
  {
    if (one.equals(two)) {
      return true;
    }
    if (one.isPrimitive()) {
      if ((one.getName().equals("int")) && (two.getName().equals("java.lang.Integer")))
      {
        return true;
      }
      if ((one.getName().equals("long")) && (two.getName().equals("java.lang.Long")))
      {
        return true;
      }
      if ((one.getName().equals("float")) && (two.getName().equals("java.lang.Float")))
      {
        return true;
      }
      if ((one.getName().equals("double")) && (two.getName().equals("java.lang.Double")))
      {
        return true;
      }
      if ((one.getName().equals("char")) && (two.getName().equals("java.lang.Character")))
      {
        return true;
      }
      if ((one.getName().equals("byte")) && (two.getName().equals("java.lang.Byte")))
      {
        return true;
      }
      if ((one.getName().equals("short")) && (two.getName().equals("java.lang.Short")))
      {
        return true;
      }
      if ((one.getName().equals("boolean")) && (two.getName().equals("java.lang.Boolean")))
      {
        return true;
      }
    }
    return false;
  }
}