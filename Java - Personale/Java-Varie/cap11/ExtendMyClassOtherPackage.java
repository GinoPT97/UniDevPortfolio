package otherPackage;

import myPackage.MyClass;

public class ExtendMyClassOtherPackage extends MyClass
{
    // accesso
    public int ex_name_private = name_private; // NON visibile
    public int ex_name_packaged = name_packaged; // NON visibile
    public int ex_name_protected = name_protected; // OK visibile
    public int ex_name_public = name_public; // OK visibile
}
