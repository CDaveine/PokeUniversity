client: main.o client.o error.o color.o
	gcc -g -o client main.o client.o error.o color.o

obj/main.o: main.c
	@mkdir -p obj
	gcc -c main.c -o obj/main.o

obj/client.o: client.c
	@mkdir -p obj
	gcc -c client.c -o obj/client.o

obj/error.o: error.c
	@mkdir -p obj
	gcc -c error.c -o obj/error.o

obj/color.o: color.c
	@mkdir -p obj
	gcc -c color.c -o obj/color.o

clean:
	@rm client
	@rm -rf obj