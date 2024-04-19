package otherPackage;

import myPackage.MyClass;

public class OtherClassOtherPackage
{
    MyClass my_class = new MyClass();
    
    // accesso
    public int ex_name_private = my_class.name_private; // NON visibile
    public int ex_name_packaged = my_class.name_packaged; // NON visibile
    public int ex_name_protected = my_class.name_protected; // NON visibile
    public int ex_name_public = my_class.name_public; // OK visibile
}
