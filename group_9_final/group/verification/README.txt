We chose JPF for our verifier.
Upon reflection this was a bad idea, JPF isn't designed to do much checking, its designed for people who want to do specific checking of Java systems.
To do this you have to implement a set of listeners and attach them to a JVM.
You then get callbacks when certain kinds of byte-code is executed and can use this information to verify things.

We found some people who had made a LTL checker for JPF called jpf-ltl.
This was done by an internship that someone did in 2009, It seems the code from then was lost and someone redid it in 2013.
We played around with it but could not get it to go.

We ended up finding an add-on called Aprop that allowed us to annotate classes with invariants that we could check all objects against.
To use aprop we found out we could only test integer values that are defined locally in the class.
Because of this we ended up having to convert lots of things from being represented as Java Objects to integers. This cluttered the code a lot but enabled us to do verification.
