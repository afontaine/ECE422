CLASSPATH = bin/


NativeInsertionSorter.dll: *.o
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
	rm -rf bin lib include