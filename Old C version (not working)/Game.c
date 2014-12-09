#include "Level.h"
#include "ActiveElement.h"
//#include "Utilities.h"

#include <stdio.h>
#include <ncurses/ncurses.h>

void draw(Level* level);

int main() {
	//printf("Started program.\n");
	Level* level = getLevel("/cygdrive/c/Users/james/Desktop/cpre185/Platformer/Esplora-Platformer/level.txt");
	printf("read the level file.\n");
	fflush(stdout);
	printf("After fflush.\n");
	//initscr();
	//refresh();
	int old_time = time(NULL);
	setSpeed(1);
	printf("Before While Loop.\n");
	while (1) {
		if (time(NULL) > old_time) {
			printf("Update.\n");
			updateLevel(level);
			printf("\tUpdated.\n");
			draw(level);
			//mvprintw(0, 0, "%d", time(NULL));
			refresh();
			old_time = time(NULL);
		}
	}
	endwin();
}

void draw(Level* level) {
	int i;
	int j;
	for (i = 0; i < level->width; i++) {
		for (j = 0; j < level->height; j++) {
			mvaddch(j, i, ' ');
		}
	}
	for (i = 0; i < level->num_items; i++) {
		LevelElement* d = (level->items)[i];
		mvprintw(30, 0, "%, %d: %c", d->x, d->y, d->representation);
		mvaddch(d->y, d->x, d->representation);
	}
	refresh();
}