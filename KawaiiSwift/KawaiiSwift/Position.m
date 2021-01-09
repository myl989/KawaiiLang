//
//  Position.m
//  KawaiiSwift
//
//  Created by myl on 2021-01-08.
//

#import <Foundation/Foundation.h>

#import "Position.h"
@implementation Position

- (Position*) init:(int) idx line:(int) ln column:(int) col fileName:(NSString*) fn fileText:(NSString*) ftxt {
    self = [super init];
    if (self) {
        self->idx = idx;
        self->ln = ln;
        self->col = col;
        self->fn = fn;
        self->ftxt = ftxt;
    }
    return self;
}

- (Position*) advance: (char) currentChar {
    idx++;
    col++;
    if (currentChar == '\n') {
        ln++;
        col = 0;
    }
    return self;
}

- (Position*) advance {
    idx++;
    col++;
    return self;
}

- (Position*) clone {
    Position* pos = [[Position alloc] init: self->idx line: self->ln column: self->col fileName: self->fn fileText: self->ftxt];
    return pos;
}

- (int) getLine {
    return self->ln;
}

- (NSString*) getFileName {
    return self->fn;
}

@end
