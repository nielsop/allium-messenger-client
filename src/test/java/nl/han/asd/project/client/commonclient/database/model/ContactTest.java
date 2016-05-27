package nl.han.asd.project.client.commonclient.database.model;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class ContactTest {

    private static Contact contact;

    private static final String PRIMARY_USERNAME = "TestContact";
    private static final String SECONDARY_USERNAME = "TestContact2";

    @BeforeClass
    public static void beforeClass() {
        contact = new Contact(PRIMARY_USERNAME);
    }

    @Test
    public void testGetUsername() {
        Assert.assertEquals(PRIMARY_USERNAME, contact.getUsername());
    }

    @Test
    public void testFromDatabaseSuccess() {
        Assert.assertEquals(contact, Contact.fromDatabase(PRIMARY_USERNAME));
    }

    @Test
    public void testEqualsIsEqualWithSameObjectTypeSameUsername() {
        final Contact otherContact = new Contact(PRIMARY_USERNAME);
        Assert.assertTrue(contact.equals(otherContact));
    }

    @Test
    public void testEqualsIsNotEqualWithSameObjectTypeDifferentUsername() {
        final Contact otherContact = new Contact(SECONDARY_USERNAME);
        Assert.assertFalse(contact.equals(otherContact));
    }

    @Test
    public void testEqualsIsNotEqualWithDifferentObjectType() {
        String s = "Test";
        Assert.assertFalse(contact.equals(s));
    }

    @Test
    public void testToStringIsEqualWithSameUsername() {
        Assert.assertEquals(PRIMARY_USERNAME, contact.toString());
    }

    @Test
    public void testToStringIsNotEqualWithDifferentUsername() {
        Assert.assertNotEquals(SECONDARY_USERNAME, contact.toString());
    }

    @Test
    public void testHashCodeIsEqualWithSameUsername() {
        final Contact otherContact = new Contact(PRIMARY_USERNAME);
        Assert.assertEquals(otherContact.hashCode(), contact.hashCode());
    }

    @Test
    public void testHashCodeIsNotEqualWithDifferentUsername() {
        final Contact otherContact = new Contact(SECONDARY_USERNAME);
        Assert.assertNotEquals(otherContact.hashCode(), contact.hashCode());
    }

}
