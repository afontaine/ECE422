CLASSPATH = bin/
ifndef JAVA_HOME
$(error JAVA_HOME is not set)
endif
ifeq ($(OS),Windows_NT)
$(info Building for Windows...)
INC = win32
TARGET = Cryptographer.dll
RMDIR = rmdir /Q /S
RM = del
else
$(info Building for Linux...)
INC = linux
TARGET = libCryptographer.so
RMDIR = rm -r
RM = rm
endif

all: Client.jar Server.jar $(TARGET)

Client.jar: Cryptographer
	jar -cfev $@ ca.afontaine.ece422.Client -C bin .

Server.jar: Cryptographer
	jar -cfev $@ ca.afontaine.ece422.Server -C bin .

$(TARGET): ca_afontaine_ece422_NativeInsertionSorter.o
	mkdir lib
	gcc -shared -o lib/$@ $^

%.o: %.c Cryptographer.h
	gcc -std=c99 -I"$(JAVA_HOME)/include" -I"$(JAVA_HOME)/include/$(INC)" -c $< -o $@

Cryptographer.h: Cryptographer
	mkdir include
	javah -classpath $(CLASSPATH) -d include ca.afontaine.ece422.$^


Cryptographer: src/ca/afontaine/ece422/*.java
	mkdir bin
	javac -d bin/ $^

PHONY: .clean

clean:
	$(RMDIR) bin lib include
	$(RM) *.o
	$(RM) *.jar
