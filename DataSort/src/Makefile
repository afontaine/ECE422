CLASSPATH = ../../out/production/DataSort


NativeInsertionSorter.dll: ca_afontaine_ece422_NativeInsertionSorter.o
	echo $(JAVA_HOME)
	gcc -shared -o $@ $^


*.o: *.c NativeInsertionSorter.h
	gcc -I$(JAVA_HOME)/include -I$(JAVA_HOME)/include/win32 -c $< -o $@

NativeInsertionSorter.h: ca.afontaine.ece422.NativeInsertionSorter.class
	javah -classpath $(CLASSPATH) $*
