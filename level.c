#include <stdio.h>
#include <windows.h>

#include "Level.h"
#include "ActiveElement.h"
#include "LevelElement.h"
#include "Utilities.h"

#include <stdio.h>

Level* getLevel(char* file_name) {
	//printf("Reading %s.\n", file_name);
	FILE* level_file;
	level_file = fopen(file_name, "r");
	//Check if the file opened correctly.
	if (level_file == NULL || feof(level_file)) {
		printf("The level file could not be opened.\n");
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
	printf("About to set the num_items of level to 0.\n");
	level->num_items = 0;
	current_row = 0;
	//while the file has other characters
	while (c > 31) {
		printf("Reading line %d.\n", current_row+1);
		current_column = 0;
		//while the file has other characters on the line
		while (c != '\n' && c > 31) {
			printf("\tReading column %d: ", current_column+1);
			printf("'%c'\n", c);
			LevelElement* item = getLevelElement(c, current_column, current_row);
			//NULL means that It should not be drawn or used in collision calculations.
			printf("\n\tGot the levelItem.\n");
			if (item != NULL) {
				printf("\tAdding the levelElement to the list.\n");
				LevelElement** listOfItems = level->items;
				printf("\tRetrieved the list of levelElements.\n");
				void*** addressOfArray = (void***)(&listOfItems);
				printf("\tGot the address of the array.\n");
				void* itemToAdd = (void*)item;
				printf("\tGot the address of the new item.\n");
				addPtr(addressOfArray, level->num_items, itemToAdd);
				printf("\tLevelElement added to the list.\n");
				(level->num_items)++;
			}
			c = fgetc(level_file);
			current_column++;
			printf("\t\tFinished the column.\n");
		}
		while (c < 31 && c != EOF)
			c = fgetc(level_file);
		current_row++;
	}
	level->width = current_column;
	level->height = current_row;
	
	return level;
}

void updateLevel(Level* level) {
	printf("\t\tIn Level.updateLevel.\n");
	int i;
	//run the update procedure, do not change the actual values
	for (i = 0; i < level->num_items; i++) {
		printf("\t\t\tAt top of loop 1.\n");
		LevelElement* platform = (level->items)[i];
		printf("\t\t\tretrieved the item to be updated (maybe): %p.\n", platform);
		getchar();
		printf("\t\t\tPlatform is_active = %d.\n", platform->is_active);
		if (platform != NULL && platform->is_active == 1) {
			printf("Updating %c.\n", platform->representation);
			ActiveElement* element = ((ActiveElement*)platform);
			element->update(element, level);
		} else {
			printf("\t\tDid not update %c.\n", platform->representation);
		}
	}
	//update the values
	for (i = 0; i < level->num_items; i++) {
		LevelElement* platform = (level->items)[i];
		if (platform->is_active) {
			ActiveElement* element = ((ActiveElement*)platform);
			element->apply(element);
		}
	}
}