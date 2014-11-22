//Header guard, prevents the header from being processed more than once
#ifndef DRAWABLE_HEADER
#define DRAWABLE_HEADER 1

typedef struct Drawable_Struct {
	char representation;
	double x;
	double y;
	//does it interact with other objects.
	int tangible;
	int is_active;
} Drawable;

#endif