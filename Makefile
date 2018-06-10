SRCS := $(wildcard *.java)
CLSS := $(SRCS:%.java=%.class)

#DBG = -g
DBG =

images: clock.png clock_back.png compass.png compass_back.png

clock.png: $(CLSS)
	java make_textures clock

compass.png: $(CLSS)
	java make_textures compass

clock_back.png: $(CLSS)
	java make_textures clock_back

compass_back.png: $(CLSS)
	java make_textures compass_back

%.class: %.java
	javac $(DBG) $<

clean:
	rm -f $(CLSS) *.png
