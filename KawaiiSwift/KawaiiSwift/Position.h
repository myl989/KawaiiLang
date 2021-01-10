//
//  Position.h
//  KawaiiSwift
//
//  Created by myl on 2021-01-08.
//

#import <Foundation/Foundation.h>

@interface Position : NSObject {
    @private
    int idx;
    @private
    int ln;
    @private
    int col;
    @private
    NSString* fn;
    @private
    NSString* ftxt;
}

- (Position*) init:(int) idx line:(int) ln column:(int) col fileName:(NSString*) fn fileText:(NSString*) ftxt;

- (Position*) advance: (char) currentChar;
- (Position*) advance;
- (Position*) clone;
- (int) getLine;
- (NSString*) getFileName;

@end
