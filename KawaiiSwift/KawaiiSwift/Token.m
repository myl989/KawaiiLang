//
//  Token.m
//  KawaiiSwift
//
//  Created by myl on 2021-01-08.
//

#import <Foundation/Foundation.h>

#import "Token.h"
@implementation Token

NSString* const TT_INT = @"INT";
NSString* const TT_FLOAT = @"FLOAT";

NSString* const TT_ADD = @"+";
NSString* const TT_MINUS = @"-";
NSString* const TT_MUL = @"ᴹ";
NSString* const TT_DIV = @"/";
NSString* const TT_MOD = @"%";
NSString* const TT_LPAREN = @"(";
NSString* const TT_RPAREN = @")";

NSString* const TT_EQUALS = @"EQUALS";
NSString* const TT_LT = @"LT";
NSString* const TT_GT = @"GT";
NSString* const TT_LTE = @"LTE";
NSString* const TT_GTE = @"GTE";

NSString* const TT_ASSIGN = @"ASSIGN";
NSString* const TT_VARNAME = @"VARNAME";
NSString* const TT_VARTYPE = @"VARTYPE";
NSString* const TT_KEYWORD = @"KEYWORD";

NSString* const TT_STARTIF = @"STARTIF";
NSString* const TT_ENDIF = @"ENDIF";

NSString* const TT_PARAM = @"UwU";
NSString* const TT_COMMA = @",";

NSString* const KEYWORDS[] = {@"OwO", @"notices", @"^_^ewndNotice", @"^_^wepeatDat", @"do", @"doWen", @"tw", @"twimes", @"ewlse", @"stawp", @"awnd", @"orw", @"nawt", @"xwr", @"dewete", @"nwthin", @"canDo", @"canGibU", @"gibU", @"^_^stawpCanDo"};

NSString* const DATA_TYPES[] = {@"Numwer"};


- (Token*) init:(NSString*) t value:(NSObject*) v {
    self = [super init];
    if (self) {
        self->type = t;
        self->value = v;
    }
    return self;
}

- (Token*) init:(NSString*) t {
    self = [super init];
    if (self) {
        self->type = t;
        self->value = NULL;
    }
    return self;
}

- (NSString*) description {
    if (value != NULL) {
        return [NSString stringWithFormat:@"%@/%s/%@", type, ":", value.description];
    } else {
        return type;
    }
}

- (BOOL)isEqualToToken:(Token*)otherToken {
    return [type isEqualToString:otherToken->type] &&
           [value isEqual:otherToken->value];
}

@end
