//
//  Variable.h
//  KawaiiSwift
//
//  Created by Albert and Mike on 2021-01-09.
//

#import <Foundation/Foundation.h>
#import "Token.m"

@interface Variable : NSObject {
    @private
    Token* type;
    @private
    NSObject* value;
}

- (Variable*) init:(Token*) type value:(NSObject*) value;
- (Token*) getType;
- (NSObject*) getValue;

@end
