#include <stdio.h>
#include <windows.h>

#include "Level.h"
#include "Drawable.h"
#include "ActiveElement.h"
#include "Utilities.h"

Level* getLevel(char* file_name) {
	FILE* level_file;
	level_file = fopen(file_name, "r");
	//Check if the file opened correctly.
	if (level_file == NULL || feof(level_file)) {
		return NULL;
	}
	Level* level = malloc(sizeof(level));
	level->items = NULL;
	//The last character read from the file.
	char c;
	//The current index of items added to the level array.
	int current_item;
	
	//The current row;
	int current_row;
	
	//The length of the last row read in.
	int current_column;
	
	c = getc(level_file);
	//read each row in a while loop:
	current_item = 0;
	current_row = 0;
	while (!feof(level_file)) {
		current_column = 0;
		while (c != '\n' && !feof(level_file)) {
			Drawable* item;
			item = (Drawable*)getLevelElement(c, current_column, current_row);
			if (item != NULL) {
				addPtr((void***)&(level->items), current_item, item);
				current_item++;
			}
			c = getc(level_file);
			current_column++;
		}
		current_row++;
	}
	level->width = current_column;
	level->height = current_row;
	return level;
}