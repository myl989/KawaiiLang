//
//  Variable.m
//  KawaiiSwift
//
//  Created by Albert and Mike on 2021-01-09.
//

#import <Foundation/Foundation.h>
#import "Token.m"
#import "Variable.h"

@implementation Variable

- (Variable*) init:(Token*) type value:(NSObject*) value {
    self = [super init];
    if (self) {
        self->type = type;
        self->value = value;
    }
    return self;
}

- (Token*) getType {
    return type;
}

- (NSObject*) getValue {
    return value;
}

@end
