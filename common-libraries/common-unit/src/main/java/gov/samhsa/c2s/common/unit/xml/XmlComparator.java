package gov.samhsa.c2s.common.unit.xml;

import org.custommonkey.xmlunit.*;
import org.custommonkey.xmlunit.examples.RecursiveElementNameAndTextQualifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.List;

public class XmlComparator {
    private static final Logger logger = LoggerFactory.getLogger(XmlComparator.class);
    public static DetailedDiff compareXMLs(String expectedResult,
                                           String actualResult, List<String> ignorableXPathsRegex) {

        DetailedDiff diff = null;
        try {
            setXMLUnitConfig();

            diff = new DetailedDiff((XMLUnit.compareXML(expectedResult,
                    actualResult)));
            diff.overrideElementQualifier(new ElementNameAndTextQualifier());
            diff.overrideElementQualifier(new ElementNameQualifier());
            diff.overrideElementQualifier(new ElementNameAndAttributeQualifier());
            diff.overrideElementQualifier(new RecursiveElementNameAndTextQualifier());

            if (ignorableXPathsRegex != null) {
                RegexBasedDifferenceListener ignorableElementsListener = new RegexBasedDifferenceListener(
                        ignorableXPathsRegex);
                /** setting our custom difference listener */
                diff.overrideDifferenceListener(ignorableElementsListener);
            }

            @SuppressWarnings("unchecked")
            List<Difference> differences = diff.getAllDifferences();
            for (Object object : differences) {
                Difference difference = (Difference) object;
                System.out.println("***********************");
                System.out.println(difference);
                System.out.println("***********************");
            }

        } catch (SAXException e) {
            logger.error(e.getMessage());
        } catch (IOException e) {
            logger.error(e.getMessage());
        }

        return diff;

    }

    private static void setXMLUnitConfig() {
        XMLUnit.setIgnoreWhitespace(Boolean.TRUE);
        XMLUnit.setIgnoreComments(Boolean.TRUE);
        XMLUnit.setIgnoreDiffBetweenTextAndCDATA(Boolean.TRUE);
        XMLUnit.setIgnoreAttributeOrder(Boolean.TRUE);
    }
}
