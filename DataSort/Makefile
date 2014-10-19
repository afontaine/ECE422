CLASSPATH = bin/
ifndef JAVA_HOME
$(error JAVA_HOME is not set)
endif
ifeq ($(OS),Windows_NT)
$(info Building for Windows...)
INC = win32
TARGET = NativeInsertionSorter.dll
RMDIR = rmdir /Q /S 
RM = del
else
$(info Building for Linux...)
INC = linux
TARGET = libNativeInsertionSorter.so
RMDIR = rm -r
RM = rm
endif

all:DataSorter.jar DataGenerator.jar $(TARGET)

DataSorter.jar: NativeInsertionSorter
	jar -cfev $@ ca.afontaine.ece422.DataSorter -C bin .

DataGenerator.jar: NativeInsertionSorter
	jar -cfev $@ ca.afontaine.ece422.DataGenerator -C bin .

$(TARGET): ca_afontaine_ece422_NativeInsertionSorter.o
	mkdir lib
	gcc -shared -o lib/$@ $^

%.o: %.c NativeInsertionSorter.h
	gcc -std=c99 -I"$(JAVA_HOME)/include" -I"$(JAVA_HOME)/include/$(INC)" -c $< -o $@

NativeInsertionSorter.h: NativeInsertionSorter
	mkdir include
	javah -classpath $(CLASSPATH) -d include ca.afontaine.ece422.$^


NativeInsertionSorter: src/ca/afontaine/ece422/*.java
	mkdir bin
	javac -d bin/ $^

PHONY: .clean

clean:
	$(RMDIR) bin lib include
	$(RM) *.o
	$(RM) *.jar


