#include "queue.h"

/* creates a new queue with a given size */
queue_t* create_queue(int capacity)
{
	queue_t* q = (queue_t*)malloc(sizeof(queue_t));
	q->capacity = capacity;
	q->data = (int**)malloc(capacity * sizeof(int*));
	q->size = 0;
	q->head = 0;
	q->tail = 0;
	
	return q;
}

/* deletes the queue and all allocated memory */
void delete_queue(queue_t *queue)
{
	free(queue->data);
	free(queue);
}

/* 
 * inserts a reference to the element into the queue
 * returns: true on success; false otherwise
 */
bool push_to_queue(queue_t *queue, void *data)
{
	//printf("BEF---H: '%i'  T: '%i'  S: '%i'\n", queue->head, queue->tail, queue->size);
	if(queue->size != queue->capacity) {
		queue->data[queue->tail] = data;
		queue->size++;
		//printf("AFT---H: '%i'  T: '%i'  S: '%i'\n", queue->head, queue->tail, queue->size);
		
		if(queue->size == queue->capacity) {
			//printf("Realloc\n");
			queue->capacity *= 2;
			
			int** tmp = realloc(queue->data, queue->capacity * sizeof(int*));
		
			if(tmp == NULL) {
				return false;
			} else {
				queue->data = tmp;
			}
		}
		
		queue->tail++;
		queue->tail %= queue->capacity;
		
		return true;
	} else {
		return false;		
	}
}

/* 
 * gets the first element from the queue and removes it from the queue
 * returns: the first element on success; NULL otherwise
 */
void* pop_from_queue(queue_t *queue)
{
	//printf("BEF---H: '%i'  T: '%i'  S: '%i'  C: '%i'\n", queue->head, queue->tail, queue->size, queue->capacity);
	if(queue->size > 0) {
		void* ret = queue->data[queue->head];
		queue->size--;
		queue->head++;
		queue->head %= queue->capacity; 
		
		
		//TODO - resize to smaller (1/3)
		if(queue->size < (queue->capacity / 3)) {
			//printf("I'm gettin' smaller\n");
			int headtemp = queue->head;
			
			// copy from buffer to start of buffer
			for(int i = 0; i < queue->size; i++) {
				queue->data[i] = queue->data[headtemp];
				headtemp++;
			}
			
			queue->capacity /= 3;
			
			// resize the data
			int** tmp = realloc(queue->data, queue->capacity * sizeof(int*));
		
			if(tmp == NULL) {
				return false;
			} else {
				queue->data = tmp;
			}
			
			queue->head = 0;
			queue->tail = queue->size;
		}
		
		//printf("AFT---H: '%i'  T: '%i'  S: '%i'  C: '%i'\n", queue->head, queue->tail, queue->size, queue->capacity);
		return ret;
	} else {
		return NULL;
	}
}

/* gets number of stored elements */
int get_queue_size(queue_t *queue)
{
	return queue->size;
}

/* 
 * gets idx-th element from the queue
 * returns the element that will be popped after idx calls of the pop_from_queue() 
 * returns: the idx-th element on success; NULL otherwise
 */
void* get_from_queue(queue_t *queue, int idx)
{
	if(get_queue_size(queue) > idx && idx >= 0) {
		int headx = (queue->head + idx % queue->capacity);
		//printf("H: '%i'   Hx: '%i'\n",queue->head, headx);
		return queue->data[headx];
	} else {
		return NULL;
	}
}
