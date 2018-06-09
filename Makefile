SRCS := $(wildcard *.java)
CLSS := $(SRCS:%.java=%.class)

#DBG = -g
DBG =

clock.png: $(CLSS)
	java make_textures

%.class: %.java
	javac $(DBG) $<

clean:
	rm -f $(CLSS) *.png
