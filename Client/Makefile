client: main.o clientbrc.o clientTCP.o game.o error.o color.o
	gcc -g -o client main.o clientbrc.o clientTCP.o game.o error.o color.o -pthread

team: team.o color.o
	gcc -g -o team team.o color.o -pthread

tchat: tchat.o
	gcc -g -o tchat tchat.o -pthread

obj/main.o: main.c
	@mkdir -p obj
	gcc -c main.c -o obj/main.o

obj/clientbrc.o: clientbrc.c
	@mkdir -p obj
	gcc -c clientbrc.c -o obj/clientbrc.o

obj/clientTCP.o: clientTCP.c
	@mkdir -p obj
	gcc -c clientTCP.c -o obj/clientTCP.o

obj/game.o: game.c
	@mkdir -p obj
	gcc -c game.c -o obj/game.o

obj/error.o: error.c
	@mkdir -p obj
	gcc -c error.c -o obj/error.o

obj/color.o: color.c
	@mkdir -p obj
	gcc -c color.c -o obj/color.o

obj/team.o: team.c
	@mkdir -p obj
	gcc -c team.c -o obj/team.o

obj/tchat.o: tchat.c
	@mkdir -p obj
	gcc -c tchat.c -o obj/tchat.o

clean:
	@rm client
	@rm -rf obj