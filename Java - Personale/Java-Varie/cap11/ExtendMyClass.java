package myPackage;

public class ExtendMyClass extends MyClass
{
    // accesso
    public int ex_name_private = name_private; // NON visibile
    public int ex_name_packaged = name_packaged; // OK visibile
    public int ex_name_protected = name_protected; // OK visibile
    public int ex_name_public = name_public; // OK visibile
}
