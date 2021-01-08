//
//  Token.m
//  KawaiiSwift
//
//  Created by myl on 2021-01-08.
//

#import <Foundation/Foundation.h>

#import "Token.h"
@implementation Token

- (Token*) init:(NSString*) t andb:(NSObject*) v {
    self = [super init];
    if (self){
        self->type = t;
        self->value = v;
    }
    return self;
}

- (Token*) init:(NSString*) t {
    self = [super init];
    if (self){
        self->type = t;
        self->value = NULL;
    }
    return self;
}

@end
