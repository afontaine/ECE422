CLASSPATH = bin/
ifndef JAVA_HOME
$(error JAVA_HOME is not set)
endif
ifeq ($(OS),Windows_NT)
$(info Building for Windows...)
INC = win32
TARGET = Cryptographer.dll
else
$(info Building for Linux...)
INC = linux
TARGET = libCryptographer.so
endif

all: Client.jar Server.jar $(TARGET)

Client.jar: Cryptographer
	jar -cfev $@ ca.afontaine.ece422.ClientView -C bin .

Server.jar: Cryptographer
	jar -cfev $@ ca.afontaine.ece422.ServerService -C bin .

$(TARGET): ca_afontaine_ece422_Cryptographer.o
	mkdir lib
	gcc -shared -o lib/$@ $^

%.o: src/%.c Cryptographer.h
	gcc -std=c99 -I"$(JAVA_HOME)/include" -I"$(JAVA_HOME)/include/$(INC)" -c $< -o $@

Cryptographer.h: Cryptographer
	mkdir src/include
	javah -classpath $(CLASSPATH) -d src/include ca.afontaine.ece422.$^


Cryptographer: src/ca/afontaine/ece422/*.java
	mkdir bin
	javac -d bin/ $^

PHONY: .clean

clean:
	rm -r bin lib src/include
	rm *.o *.jar
