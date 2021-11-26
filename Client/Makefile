client: main.o clientbrc.o clientTCP.o error.o color.o
	gcc -g -o client main.o clientbrc.o clientTCP.o error.o color.o

obj/main.o: main.c
	@mkdir -p obj
	gcc -c main.c -o obj/main.o

obj/clientbrc.o: clientbrc.c
	@mkdir -p obj
	gcc -c clientbrc.c -o obj/clientbrc.o

obj/clientTCP.o: clientTCP.c
	@mkdir -p obj
	gcc -c clientTCP.c -o obj/clientTCP.o

obj/error.o: error.c
	@mkdir -p obj
	gcc -c error.c -o obj/error.o

obj/color.o: color.c
	@mkdir -p obj
	gcc -c color.c -o obj/color.o

clean:
	@rm client
	@rm -rf obj