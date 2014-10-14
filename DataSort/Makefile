CLASSPATH = bin/


NativeInsertionSorter.dll: ca_afontaine_ece422_NativeInsertionSorter.o
	mkdir lib
	gcc -shared -o lib/$@ $^


%.o: %.c NativeInsertionSorter.h
	gcc -std=c99 -I"$(JAVA_HOME)/include" -I"$(JAVA_HOME)/include/win32" -c $< -o $@

NativeInsertionSorter.h: NativeInsertionSorter
	mkdir include
	javah -classpath $(CLASSPATH) -d include ca.afontaine.ece422.$^


NativeInsertionSorter: src/ca/afontaine/ece422/*.java
	mkdir bin
	javac -d bin/ $^

PHONY: .clean

clean:
	rmdir /Q /S bin lib include
	del *.o
