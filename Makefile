SRCS := $(wildcard *.java)
CLSS := $(SRCS:%.java=%.class)

#DBG = -g
DBG =

images: clock.png compass.png

clock.png: $(CLSS)
	java make_textures clock

compass.png: $(CLSS)
	java make_textures compass

%.class: %.java
	javac $(DBG) $<

clean:
	rm -f $(CLSS) *.png
