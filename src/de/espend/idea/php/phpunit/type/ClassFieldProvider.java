package de.espend.idea.php.phpunit.type;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.ResolveResult;
import com.intellij.psi.util.PsiElementFilter;
import com.intellij.psi.util.PsiTreeUtil;
import com.jetbrains.php.PhpIndex;
import com.jetbrains.php.lang.parser.parsing.classes.ClassField;
import com.jetbrains.php.lang.psi.elements.*;
import com.jetbrains.php.lang.psi.resolve.types.PhpType;
import com.jetbrains.php.lang.psi.resolve.types.PhpTypeProvider3;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Set;

/**
 * $this->prophesize(Foobar::class)->find()->will<caret>Return;
 *
 * @author Daniel Espendiller <daniel@espendiller.net>
 */
public class ClassFieldProvider implements PhpTypeProvider3 {
    public static final char SIGNATURE_KEY = '\u1533';
    public static char TRIM_KEY = '\u1536';

    @Override
    public char getKey() {
        return '\u1539';
    }

    @Nullable
    @Override
    public PhpType getType(PsiElement psiElement) {
        if(!(psiElement instanceof FieldReference)) {
            return null;
        }

        String signature = ((FieldReference) psiElement).getSignature();
        PhpClass phpClass = PsiTreeUtil.getParentOfType(psiElement, PhpClass.class);
        if(phpClass == null) {
            return null;
        }

        Method setUp = phpClass.findMethodByName("setUp");

        PsiElement[] psiElements = PsiTreeUtil.collectElements(setUp, new PsiElementFilter() {
            @Override
            public boolean isAccepted(PsiElement psiElement) {
                return psiElement instanceof FieldReference && ((FieldReference) psiElement).getName().equals(((FieldReference) psiElement).getName());
            }
        });

        for (PsiElement element : psiElements) {
            if(element instanceof FieldReference) {
                String signature1 = ((FieldReference) element).getSignature();
                return new PhpType().add("#" + this.getKey() + signature1);
            }
        }

        return null;
    }

    @Override
    public Collection<? extends PhpNamedElement> getBySignature(String expression, Set<String> visited, int depth, Project project) {
        Collection<? extends PhpNamedElement> anyByFQN = PhpIndex.getInstance(project).getBySignature(expression);

        for (PhpNamedElement phpNamedElement : anyByFQN) {
            PhpClass phpClass = PsiTreeUtil.getParentOfType(phpNamedElement, PhpClass.class);
            Method setUp = phpClass.findMethodByName("setUp");

            PsiElement[] psiElements = PsiTreeUtil.collectElements(setUp, new PsiElementFilter() {
                @Override
                public boolean isAccepted(PsiElement psiElement) {
                    return psiElement instanceof FieldReference && ((FieldReference) psiElement).getName().equals(((FieldReference) psiElement).getName());
                }
            });

            for (PsiElement element : psiElements) {
                if(element instanceof FieldReference) {

                    Set<String> types = ((FieldReference) element).getType().elementType().getTypes();
                    System.out.println(types);
                }
            }
        }

        return PhpIndex.getInstance(project).getAnyByFQN("\\DateTime");
    }
}
